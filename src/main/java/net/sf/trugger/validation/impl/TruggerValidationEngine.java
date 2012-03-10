/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.trugger.validation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.trugger.Result;
import net.sf.trugger.bind.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.message.Message;
import net.sf.trugger.message.MessageCreator;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.validation.InvalidElement;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationBridge;
import net.sf.trugger.validation.ValidationEngine;
import net.sf.trugger.validation.ValidationResult;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;
import net.sf.trugger.validation.ValidatorInvoker;

/**
 * The default validation engine.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerValidationEngine implements ValidationEngine {

  private final ValidatorFactory validatorFactory;
  private final MessageCreator messageCreator;
  private String context = null;

  /**
   * Creates a new engine for validation.
   *
   * @param messageCreator
   *          the component to create the messages for the invalid properties.
   * @param validatorFactory
   *          the component to creates {@link Validator} instances.
   */
  public TruggerValidationEngine(ValidatorFactory validatorFactory, MessageCreator messageCreator) {
    this.messageCreator = messageCreator;
    this.validatorFactory = validatorFactory;
  }

  @Override
  public ValidationEngine forContext(String context) {
    this.context = context;
    return this;
  }

  public Result<ValidationResult, Object> selection(ElementsSelector selector) {
    return new ValidationRequest(selector);
  }

  public Result<ValidationResult, Object> selection(ElementSelector selector) {
    return new ValidationRequest(selector);
  }

  public Result<ValidationResult, Object> allElements() {
    return new ValidationRequest();
  }

  public Result<ValidationResult, Object> element(Element element) {
    return new ValidationRequest(element);
  }

  private class ValidationRequest implements Result<ValidationResult, Object> {

    private ElementSelector elementSelector;
    private ElementsSelector elementsSelector;
    private Element element;

    private ValidationRequest() {

    }

    private ValidationRequest(Element element) {
      this.element = element;
    }

    private ValidationRequest(ElementsSelector elementsSelector) {
      this.elementsSelector = elementsSelector;
    }

    private ValidationRequest(ElementSelector elementSelector) {
      this.elementSelector = elementSelector;
    }

    public ValidationResult in(Object target) {
      Collection<Element> elements;
      if (elementsSelector != null) {
        elements = elementsSelector.annotated().in(target);
      } else if (elementSelector != null) {
        elements = new ArrayList<Element>();
        Element p = elementSelector.annotated().in(target);
        if (p != null) {
          elements.add(p);
        }
      } else if(element != null) {
        elements = Arrays.asList(element);
      } else {
        elements = Elements.elements().annotated().in(target);
      }
      return validate(target, elements);
    }

    private ValidationResult validate(Object target, Collection<Element> elements) {
      Set<Element> currentValidElements = new HashSet<Element>();
      Set<Element> currentInvalidElements = new HashSet<Element>();

      Collection<InvalidElement> invalidElements = new ArrayList<InvalidElement>(20);
      Collection<Element> elementsToValidate = new ArrayList<Element>(elements);

      for (Element prop : elementsToValidate) {
        validate(new ValidationParameter(target, prop, currentInvalidElements, currentValidElements, invalidElements));
      }

      return new TruggerValidationResult(invalidElements, target);
    }

    private void validate(ValidationParameter parameter) {
      Element element = parameter.element;
      Object value = null;
      Annotation[] annotations = element.getAnnotations();
      boolean valid = true;
      boolean valueGetted = false; //for lazy get
      List<Message> messages = parameter.messages;
      for (Annotation annotation : annotations) {
        ValidatorContext validatorContext = new ValidatorContextImpl(annotation, element, parameter.target, context);
        if (validatorFactory.canCreate(validatorContext)) {
          //checks the context (if defined)
          if(context != null) {
            Element ctxElement = Elements.element("context").in(annotation);
            if((ctxElement != null) && String[].class.equals(ctxElement.type())) {
              String[] ctxValues = ctxElement.value();
              if((ctxValues.length > 0) && !Arrays.asList(ctxValues).contains(context)) {
                continue;
              }
            }
          }
          ValidatorInvoker validator = newValidator(parameter, validatorContext);
          if (!valueGetted) {
            value = element.in(parameter.target).value();
            valueGetted = true;
          }
          if (!validator.isValid(value)) {
            valid = false;
            Message message = messageCreator.createMessage(element, annotation, parameter.target);
            if (message != null) {
              messages.add(message);
            }
          }
        }
      }
      if (!valid) {
        parameter.invalidElements.add(new TruggerInvalidElement(element, value, parameter.target, messages));
        parameter.currentInvalidElements.add(element);
      } else {
        parameter.currentValidElements.add(element);
      }
    }

  }

  /**
   * A class to be the bridge between the validator and the object validator.
   */
  private class Bridge implements ValidationBridge {

    private final ValidationParameter parameter;

    public Bridge(ValidationParameter parameter) {
      this.parameter = parameter;
    }

    public void addInvalidElements(Collection<InvalidElement> invalidElements) {
      parameter.invalidElements.addAll(invalidElements);
    }

    public void addInvalidElement(InvalidElement invalidElement) {
      parameter.invalidElements.add(invalidElement);
    }

    public ValidationEngine validate() {
      Validation validator = new Validation(messageCreator);
      return validator.validate();
    }

    public Validator createValidator(Element element) {
      CompositeValidator validator = new CompositeValidator();
      for (Annotation nestedAnnotation : element.getDeclaredAnnotations()) {
        ValidatorContext context = new ValidatorContextImpl(nestedAnnotation, element, parameter.target, TruggerValidationEngine.this.context);
        if (validatorFactory.canCreate(context)) {
          validator.add(newValidator(parameter, context));
        }
      }
      return validator;
    }

    public boolean validate(AnnotatedElement annotatedElement, Object value) {
      boolean valid = true;
      for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
        ValidatorContext context = new ValidatorContextImpl(annotation, parameter.element, parameter.target, TruggerValidationEngine.this.context);
        if (validatorFactory.canCreate(context)) {
          ValidatorInvoker validator = newValidator(parameter, context);
          if (!validator.isValid(value)) {
            Message message = messageCreator.createMessage(parameter.element, annotation, parameter.target);
            parameter.messages.add(message);
            valid = false;
          }
        }
      }
      return valid;
    }

  }

  private ValidatorInvoker newValidator(ValidationParameter parameter, ValidatorContext context) {
    ValidatorInvoker invoker = validatorFactory.create(context);
    Binder binder = Bind.newBinder();
    Bridge bridge = new Bridge(parameter);
    binder.use(new ValidatorReferenceResolver(bridge)).toElements().ofType(Validator.class);
    binder.bind(bridge).toElements().ofType(ValidationBridge.class);
    binder.applyBinds(invoker.validator());
    return invoker;
  }

}
