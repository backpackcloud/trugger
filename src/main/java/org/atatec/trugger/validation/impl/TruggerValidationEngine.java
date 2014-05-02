/*
 * Copyright 2009-2014 Marcelo Guimarães
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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.Result;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.validation.*;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of a validation engine.
 * <p>
 * This class checks against constraints that defines
 * {@link org.atatec.trugger.validation.Validator validators} to check them.
 *
 * @since 5.1
 */
public class TruggerValidationEngine implements ValidationEngine {

  private final ValidatorFactory factory;

  /**
   * Creates a new engine using the given factory to create validators.
   *
   * @param factory the factory to use for creating validators
   */
  public TruggerValidationEngine(ValidatorFactory factory) {
    this.factory = factory;
  }

  @Override
  public Result<ValidationResult, Object> validate(ElementsSelector selector) {
    return (target) -> {
      ValidationResultImpl result = new ValidationResultImpl(target);
      for (Element element : selector.in(target)) {
        Object value = null;
        NestedValidationEngine engine =
            new NestedValidationEngine(result, element, target);
        InvalidElementImpl invalidElement =
            new InvalidElementImpl(element, value);
        boolean valid = true;
        boolean valueProcessed = false;
        for (Annotation annotation : element.getAnnotations()) {
          if (!valueProcessed) {
            value = element.in(target).get();
            engine.setValue(value);
            valueProcessed = true;
          }
          Validator validator = factory
              .create(annotation, element, target, engine);
          if (validator != null && !validator.isValid(value)) {
            invalidElement.violatedConstraints
                .put(annotation.annotationType(), annotation);
            valid = false;
          }
        }
        if (!valid) {
          result.invalidElements.put(invalidElement.name(), invalidElement);
        }
      }
      return result;
    };
  }

  private class NestedValidationEngine implements ValidationEngine {

    private final ValidationResultImpl result;
    private final Element element;
    private final Object target;
    private Object value;

    public NestedValidationEngine(ValidationResultImpl result,
                                  Element element,
                                  Object target) {
      this.result = result;
      this.element = element;
      this.target = target;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    @Override
    public Result<ValidationResult, Object> validate(ElementsSelector selector) {
      return _target -> {
        ValidationResult _result = TruggerValidationEngine.this
            .validate(selector).in(_target);
        if (_result.target() == value) {
          String parent = element.name();
          for (InvalidElement invalidElement : _result.invalidElements()) {
            String newName = String.format(
                "%s.%s", parent, invalidElement.name()
            );
            Element nestedElement = Elements.element(newName).in(target);
            result.invalidElements.put(newName,
                new InvalidElementImpl(
                    nestedElement,
                    invalidElement.invalidValue(),
                    invalidElement.violatedConstraints())
            );
          }
        }
        return _result;
      };
    }
  }

  private class ValidationResultImpl implements ValidationResult {

    private final Object target;
    private final Map<String, InvalidElement> invalidElements;

    private ValidationResultImpl(Object target) {
      this.target = target;
      this.invalidElements = new HashMap<>();
    }

    @Override
    public boolean isValid() {
      return invalidElements.isEmpty();
    }

    @Override
    public boolean isElementInvalid(String name) {
      return invalidElements.containsKey(name);
    }

    @Override
    public Object target() {
      return target;
    }

    @Override
    public InvalidElement invalidElement(String name) {
      return invalidElements.get(name);
    }

    @Override
    public Collection<InvalidElement> invalidElements() {
      return Collections.unmodifiableCollection(invalidElements.values());
    }
  }

  private class InvalidElementImpl implements InvalidElement {

    private final Element element;
    private final Object invalidValue;
    private final Map<Class<? extends Annotation>, Annotation> violatedConstraints;

    private InvalidElementImpl(Element element, Object invalidValue) {
      this.element = element;
      this.invalidValue = invalidValue;
      this.violatedConstraints = new HashMap<>();
    }

    private InvalidElementImpl(
        Element element,
        Object invalidValue,
        Collection<Annotation> violatedConstraints) {
      this.element = element;
      this.invalidValue = invalidValue;
      this.violatedConstraints = new HashMap<>();
      for (Annotation violatedConstraint : violatedConstraints) {
        this.violatedConstraints
            .put(violatedConstraint.annotationType(), violatedConstraint);
      }
    }

    @Override
    public Annotation violatedConstraint(Class<? extends Annotation> constraint) {
      return violatedConstraints.get(constraint);
    }

    @Override
    public boolean isConstraintViolated(Class<? extends Annotation> constraint) {
      return violatedConstraints.containsKey(constraint);
    }

    @Override
    public Object invalidValue() {
      return invalidValue;
    }

    @Override
    public Collection<Annotation> violatedConstraints() {
      return Collections.unmodifiableCollection(violatedConstraints.values());
    }

    @Override
    public Class declaringClass() {
      return element.declaringClass();
    }

    @Override
    public String name() {
      return element.name();
    }

    @Override
    public Class type() {
      return element.type();
    }

    @Override
    public boolean isReadable() {
      return element.isReadable();
    }

    @Override
    public boolean isWritable() {
      return element.isWritable();
    }

    @Override
    public ValueHandler in(Object target) {
      return element.in(target);
    }

    @Override
    public boolean isSpecific() {
      return element.isSpecific();
    }

    @Override
    public <E> E target() {
      return element.target();
    }

    @Override
    public <E> E get() throws HandlingException {
      return element.get();
    }

    @Override
    public void set(Object value) throws HandlingException {
      element.set(value);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return element.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
      return element.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
      return element.getDeclaredAnnotations();
    }
  }

}
