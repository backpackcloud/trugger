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
package org.atatec.trugger.test.validator;

import java.util.Date;

import org.atatec.trugger.validation.validator.Pattern;

import org.junit.Test;

public class PatternValidatorTest extends ValidatorTest<Pattern> {

  @Test
  public void testPattern() {
    builder.map("\\d+").to(annotation.value());

    createValidator();

    assertValid("146356");
    assertInvalid(new StringBuilder("146 356"));
    assertValid(null);
    assertTypeDisallowed(new Object());
    assertTypeDisallowed(new Date());
  }

  @Test
  public void testPatternWithFlags() {
    builder.map("[a-g]+").to(annotation.value());
    builder.map(java.util.regex.Pattern.CASE_INSENSITIVE).to(annotation.flags());

    createValidator();

    assertValid("abcGCD");
    assertInvalid("aBcK");
    assertTypeDisallowed(new Object());
    assertTypeDisallowed(new Date());
  }
}
