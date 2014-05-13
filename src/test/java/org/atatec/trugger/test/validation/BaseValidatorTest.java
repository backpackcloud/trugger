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

package org.atatec.trugger.test.validation;

import org.atatec.trugger.ObjectMapper;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.util.mock.AnnotationMock;
import org.atatec.trugger.validation.*;
import org.junit.Before;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimarães
 */
public abstract class BaseValidatorTest<E extends Annotation> {

  private final ValidatorFactory factory = Validation.factory();
  private final Class<E> constraintType;
  private AnnotationMock<E> builder;
  protected E constraint;
  private Validator validator;

  protected BaseValidatorTest() {
    constraintType = Reflection.reflect().genericType("E").in(this);
  }

  @Before
  public void initialize() {
    builder = new AnnotationMock<>(constraintType);
    constraint = builder.annotation();
  }

  protected final ObjectMapper<Object, AnnotationMock<E>> map(Object value) {
    return builder.map(value);
  }

  protected void assertValid(Object value) {
    if (validator == null) {
      createValidator();
    }
    assertTrue(validator.isValid(value));
  }

  protected void assertInvalid(Object value) {
    if (validator == null) {
      createValidator();
    }
    assertFalse(validator.isValid(value));
  }

  protected final void createValidator() {
    validator = factory.create(builder.createMock());
  }

  protected final Validator newValidator() {
    return factory.create(builder.createMock());
  }

  protected final void createValidator(Element element, Object target) {
    validator = factory.create(builder.createMock(), element, target, Validation.engine());
  }

  protected final void testResultElements(ValidationResult result) {
    for (InvalidElement invalidElement : result.invalidElements()) {
      assertTrue(result.isElementInvalid(invalidElement.name()));
    }
  }

}
