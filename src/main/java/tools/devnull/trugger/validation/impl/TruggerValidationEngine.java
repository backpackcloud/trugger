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

package tools.devnull.trugger.validation.impl;

import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.Elements;
import tools.devnull.trugger.validation.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation of a validation engine.
 * <p>
 * This class checks against constraints that defines
 * {@link tools.devnull.trugger.validation.Validator validators} to check them.
 *
 * @since 5.1
 */
public class TruggerValidationEngine implements ValidationEngine {

  private final ValidatorFactory factory;
  private final Predicate<Element> filter;

  /**
   * Creates a new engine using the given factory to create validators.
   *
   * @param factory the factory to use for creating validators
   */
  public TruggerValidationEngine(ValidatorFactory factory) {
    this(factory, el -> true);
  }

  private TruggerValidationEngine(ValidatorFactory factory,
                                  Predicate<Element> filter) {
    this.factory = factory;
    this.filter = filter;
  }

  @Override
  public ValidationEngine filter(Predicate<Element> filter) {
    return new TruggerValidationEngine(factory, filter);
  }

  @Override
  public ValidationResult validate(Object target) {
    if (target instanceof List) {
      ValidationResultImpl result = new ValidationResultImpl(target);
      int i = 0;
      for (Object o : (List) target) {
        result.add(i++, o, _validate(o));
      }
      return result;
    } else if (target.getClass().isArray()) {
      ValidationResultImpl result = new ValidationResultImpl(target);
      int i = 0;
      for (Object o : (Object[]) target) {
        result.add(i++, o, _validate(o));
      }
      return result;
    }
    return _validate(target);
  }

  private ValidationResultImpl _validate(Object target) {
    ValidationResultImpl result = new ValidationResultImpl(target);
    List<Element> elements = Elements.elements().filter(filter).in(target);
    ValidationEngine engine;
    for (Element element : elements) {
      Object value = null;
      InvalidElementImpl invalidElement = null;
      boolean valid = true;
      boolean valueProcessed = false;
      for (Annotation annotation : element.getAnnotations()) {
        Class<? extends Annotation> type = annotation.annotationType();
        if (type.isAnnotationPresent(MergeElements.class)) {
          engine = new MergeValidationEngine(
              this, result.invalidElements, element, filter, target
          );
        } else {
          engine = this;
        }
        Validator validator = factory.create(annotation, element, target, engine);
        if (validator != null) {
          if (!valueProcessed) {
            value = element.in(target).value();
            valueProcessed = true;
          }
          if (!validator.isValid(value)) {
            if (invalidElement == null) {
              invalidElement = new InvalidElementImpl(element, value);
            }
            invalidElement.addViolatedConstraint(type, annotation);
            valid = false;
          }
        }
      }
      if (!valid) {
        result.invalidElements.put(invalidElement.name(), invalidElement);
      }
    }
    return result;
  }

}
