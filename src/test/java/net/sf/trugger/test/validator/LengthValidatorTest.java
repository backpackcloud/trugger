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

import java.util.Date;

import net.sf.trugger.validation.validator.Length;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class LengthValidatorTest extends ValidatorTest<Length> {

  @Test
  public void testTrim() {
    builder.map(3).to(annotation.min());
    builder.map(5).to(annotation.max());
    builder.map(true).to(annotation.trim());
    createValidator();

    assertValid(null);
    assertInvalid("   ");
    assertInvalid("     ");
    assertValid(new StringBuilder("123"));
    assertValid("1  45");
    assertValid("1234  ");
    assertValid("   1234  ");
    assertInvalid(new StringBuilder("123456789"));
  }

  @Test
  public void testNoTrim() {
    builder.map(3).to(annotation.min());
    builder.map(5).to(annotation.max());
    builder.map(false).to(annotation.trim());
    createValidator();

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(new Date());

    assertValid(null);
    assertValid("   ");
    assertValid("     ");
    assertValid(new StringBuilder("123"));
    assertValid("12  5");
    assertInvalid("1234  ");
    assertInvalid(new StringBuilder("123456789"));
    assertInvalid("");
    assertInvalid(" ");
    assertInvalid("  ");
  }

}
