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

package org.atatec.trugger.validation.validator;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorFactory;

import java.lang.annotation.Annotation;

/**
 * Validator that validates a group of constraints inside a constraint.
 * <p>
 * To use this validator, just annotate the annotation with the constraints like
 * the example bellow:
 * <p>
 * <pre>{@code
 *  @NotNull
 *  @NotEmpty
 *  @SomeConstraint
 *  @ValidatorClass(DomainValidator.class)
 *  @Retention(RetentionPolicy.RUNTIME)
 *  public @interface MyConstraint {
 *  }
 * }
 * </pre>
 * This validator is useful to create domain constraints and simplify coding.
 *
 * @since 5.1
 */
public class DomainValidator implements Validator {

  private final ValidatorFactory factory;
  private final Annotation annotation;
  private final Object target;
  private final Element element;
  private final ValidationEngine engine;

  public DomainValidator(ValidatorFactory factory, Annotation annotation,
                         Object target, Element element,
                         ValidationEngine engine) {
    this.factory = factory;
    this.annotation = annotation;
    this.target = target;
    this.element = element;
    this.engine = engine;
  }

  @Override
  public boolean isValid(Object value) {
    Validator validator;
    for (Annotation a : annotation.annotationType().getDeclaredAnnotations()) {
      validator = factory.create(a, element, target, engine);
      if (validator != null && !validator.isValid(value)) {
        return false;
      }
    }
    return true;
  }
}
