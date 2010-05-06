/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.validator;

import static net.sf.trugger.util.mock.Mock.annotation;
import static net.sf.trugger.util.mock.Mock.mock;

import java.lang.annotation.Annotation;

import junit.framework.Assert;
import net.sf.trugger.element.Element;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.util.mock.AnnotationMockBuilder;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;
import net.sf.trugger.validation.impl.ValidatorContextImpl;

import org.junit.Before;

/**
 * A base class for testing the validators that don't use bindings.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class ValidatorTest<T extends Annotation> {

  protected final ValidatorFactory factory = Validation.newValidatorFactory();
  protected Validator validator;
  protected T annotation;
  protected AnnotationMockBuilder<T> builder;
  private final Class<T> annotationType;

  public ValidatorTest() {
    annotationType = Reflection.reflect().genericType("T").in(this);
  }

  @Before
  public void initialize() {
    builder = new AnnotationMockBuilder<T>(annotationType);
    annotation = builder.annotation();
    validator = null;
  }

  protected final void createValidator(ValidatorContext context) {
    validator = factory.create(context);
  }

  protected final void createValidator(Annotation annotation, Object target) {
    validator = factory.create(new ValidatorContextImpl(annotation, target));
  }

  protected final void createValidator(Element element, Object target) {
    validator = factory.create(new ValidatorContextImpl(builder.mock(), element, target));
  }

  protected final void createValidator(Object target) {
    validator = factory.create(new ValidatorContextImpl(builder.mock(), null, target));
  }

  protected final void createValidator(Class<? extends Annotation> annotationType) {
    validator = factory.create(new ValidatorContextImpl(mock(annotation(annotationType))));
  }

  protected final void createValidator() {
    validator = factory.create(new ValidatorContextImpl(builder.mock()));
  }

  protected final void assertTypeDisallowed(Object value) {
    if(validator == null) {
      createValidator();
    }
    boolean error = false;
    try {
      validator.isValid(value);
    } catch (IllegalArgumentException e) {
      error = true;
    }
    Assert.assertTrue(error);
  }

  protected final void assertValid(Object value) {
    if(validator == null) {
      createValidator();
    }
    Assert.assertTrue(validator.isValid(value));
  }

  protected final void assertInvalid(Object value) {
    if(validator == null) {
      createValidator();
    }
    Assert.assertFalse(validator.isValid(value));
  }

}
