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
package net.sf.trugger.test.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.sf.trugger.util.mock.AnnotationMockBuilder;
import net.sf.trugger.validation.ValidationContext;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorClass;
import net.sf.trugger.validation.validator.NotNull;
import net.sf.trugger.validation.validator.Range;
import net.sf.trugger.validation.validator.Valid;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ValidValidatorTest extends ValidatorTest<Valid> {

  static class TestObject {

    @NotNull(context = {"simple", "full"})
    private String string;

    @Range(min = 2, context = "full")
    private int count;

    public TestObject() {}

    public TestObject(String string, int count) {
      this.string = string;
      this.count = count;
    }

  }

  @Test
  public void testValidator() {
    createValidator();
    assertInvalid(new TestObject());
    assertValid(new TestObject("str", 3));
  }

  @Test
  public void testValidatorWithContext() {
    builder.map("simple").to(annotation.forContext());
    createValidator();
    assertValid(new TestObject("str", 0));
    assertInvalid(new TestObject(null, 3));
  }

  static class ContextTest {

    @ContextValidation
    String context;

  }

  @ValidatorClass(ContextValidator.class)
  @Retention(RetentionPolicy.RUNTIME)
  static @interface ContextValidation {}

  static class ContextValidator implements Validator {

    @ValidationContext
    static String context;

    public boolean isValid(Object value) {
      if((value == null) || (context == null)) {
        return value == context;
      }
      return value != null ? value.equals(context) : context.equals(value);
    }

  }

  @Test
  public void testContextPropagation() {
    Valid annotation = new AnnotationMockBuilder<Valid>(){{
      map("simple").to(annotation.forContext());
    }}.mock();
    ContextTest test = new ContextTest();
    test.context = "simple";
    createValidator(annotation, annotation);
    assertValid(test);
    test.context = "full";
    assertInvalid(test);
  }

}
