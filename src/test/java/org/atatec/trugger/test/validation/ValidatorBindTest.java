/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.bind.Binder;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorBinder;
import org.atatec.trugger.validation.ValidatorClass;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.impl.ValidatorContextImpl;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.util.mock.Mock.annotation;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ValidatorBindTest {

  private Object object = new Object();
  private ValidatorBinder binder;

  @Before
  public void initialize() {
    binder = new ValidatorBinder() {
      public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
        binder.bind(object).to(field("testField"));
      }
    };
    Validation.plug(binder);
  }

  @Test
  public void testBinder() {
    ValidatorFactory factory = Validation.newValidatorFactory();
    TestValidator validator = (TestValidator) factory.create(new ValidatorContextImpl(mock(annotation(TestValidation.class)))).validator();
    assertSame(object, validator.testField);

    Validation.unplug(binder);

    validator = (TestValidator) factory.create(new ValidatorContextImpl(mock(annotation(TestValidation.class)))).validator();
    assertNull(validator.testField);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @ValidatorClass(TestValidator.class)
  public @interface TestValidation {}

  public static class TestValidator implements Validator {
    private Object testField;

    public boolean isValid(Object value) {
      return true;
    }
  }

}
