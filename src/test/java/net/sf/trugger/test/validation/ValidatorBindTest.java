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
package net.sf.trugger.test.validation;

import static net.sf.trugger.util.mock.Mock.annotation;
import static net.sf.trugger.util.mock.Mock.mock;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.sf.trugger.bind.Binder;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorBinder;
import net.sf.trugger.validation.ValidatorClass;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;
import net.sf.trugger.validation.impl.ValidatorContextImpl;

import org.junit.Before;
import org.junit.Test;

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
        binder.bind(object).toField("testField");
      }
    };
    Validation.plug(binder);
  }

  @Test
  public void testBinder() {
    ValidatorFactory factory = Validation.newValidatorFactory();
    TestValidator validator = (TestValidator) factory.create(new ValidatorContextImpl(mock(annotation(TestValidation.class))));
    assertSame(object, validator.testField);

    Validation.unplug(binder);

    validator = (TestValidator) factory.create(new ValidatorContextImpl(mock(annotation(TestValidation.class))));
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
