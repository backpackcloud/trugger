/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.validation.impl;

import org.atatec.trugger.bind.Bind;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.message.Message;
import org.atatec.trugger.message.MessageCreator;
import org.atatec.trugger.message.Messages;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationBridge;
import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.ValidationResult;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static org.atatec.trugger.element.Elements.elements;

/**
 * The default validation engine.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerValidationEngine implements ValidationEngine {

  private MessageCreator messageCreator;
  private String context = null;
  private ValidatorFactory validatorFactory;

  /**
   * Creates a new engine for validation.
   *
   * @param factory the component to creates {@link Validator} instances.
   */
  public TruggerValidationEngine(ValidatorFactory factory) {
    this.validatorFactory = factory;
    this.messageCreator = Messages.createMessageCreator();
  }

  @Override
  public ValidationEngine using(MessageCreator messageCreator) {
    this.messageCreator = messageCreator;
    return this;
  }

  @Override
  public ValidationEngine using(ResourceBundle resourceBundle) {
    this.messageCreator = Messages.createMessageCreator(resourceBundle);
    return this;
  }

  @Override
  public ValidationEngine forContext(String context) {
    this.context = context;
    return this;
  }

  @Override
  public ValidationResult validate(Object target) {
    return validate(elements().in(target));
  }

  @Override
  public ValidationResult validate(Element element) {
    return validate(Arrays.asList(element));
  }

  @Override
  public ValidationResult validate(Collection<Element> elements) {
    Set<Element> currentValidElements = new HashSet<Element>();
    Set<Element> currentInvalidElements = new HashSet<Element>();

    Collection<InvalidElement> invalidElements = new ArrayList<InvalidElement>(20);
    Collection<Element> elementsToValidate = new ArrayList<Element>(elements);

    for (Element element : elementsToValidate) {
      validate(new ValidationParameter(element.target(), element, currentInvalidElements, currentValidElements, invalidElements));
    }

    return new TruggerValidationResult(invalidElements, elements.iterator().next().target());
  }

  private void validate(ValidationParameter parameter) {
    Element element = parameter.element;
    Object value = null;
    Annotation[] annotations = element.getAnnotations();
    boolean valid = true;
    boolean gotValue = false; //for lazy get
    List<Message> messages = parameter.messages;
    for (Annotation annotation : annotations) {
      ValidatorContext validatorContext = new ValidatorContextImpl()
        .annotation(annotation)
        .element(element)
        .target(parameter.target)
        .context(context);
      if (validatorFactory.canCreate(validatorContext)) {
        //checks the context (if defined)
        if (context != null) {
          Element ctxElement = Elements.element("context").in(annotation);
          if ((ctxElement != null) && String[].class.equals(ctxElement.type())) {
            String[] ctxValues = ctxElement.value();
            if ((ctxValues.length > 0) && !Arrays.asList(ctxValues).contains(context)) {
              continue;
            }
          }
        }
        ValidatorInvoker validator = newValidator(parameter, validatorContext);
        if (!gotValue) {
          value = element.in(parameter.target).value();
          gotValue = true;
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

  /** A class to be the bridge between the validator and the object validator. */
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

    public ValidationEngine validation() {
      return Validation.validation().using(messageCreator);
    }

    public Validator createValidator(Element element) {
      CompositeValidator validator = new CompositeValidator();
      for (Annotation nestedAnnotation : element.getDeclaredAnnotations()) {
        ValidatorContext context = new ValidatorContextImpl()
          .annotation(nestedAnnotation)
          .element(element)
          .target(parameter.target)
          .context(TruggerValidationEngine.this.context);
        if (validatorFactory.canCreate(context)) {
          validator.add(newValidator(parameter, context));
        }
      }
      return validator;
    }

    public boolean validate(AnnotatedElement annotatedElement, Object value) {
      boolean valid = true;
      for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
        ValidatorContext context = new ValidatorContextImpl()
          .annotation(annotation)
          .element(parameter.element)
          .target(parameter.target)
          .context(TruggerValidationEngine.this.context);
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
    Bridge bridge = new Bridge(parameter);
    Bind.binds()
      .use(new ValidatorReferenceResolver(bridge)).in(elements().ofType(Validator.class))
      .bind(bridge).to(elements().ofType(ValidationBridge.class))
      .applyIn(invoker.validator());
    return invoker;
  }

}
