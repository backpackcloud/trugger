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

import org.atatec.trugger.validation.validator.Pattern;
import org.junit.Test;

/**
 * @author Marcelo Guimarães
 */
public class PatternValidatorTest extends BaseValidatorTest<Pattern> {

  @Test
  public void testPattern() {
    map("\\d+").to(constraint.value());

    assertValid("146356");
    assertInvalid(new StringBuilder("146 356"));
    assertValid(null);
  }

  @Test
  public void testPatternWithFlags() {
    map("[a-g]+").to(constraint.value());
    map(java.util.regex.Pattern.CASE_INSENSITIVE).to(constraint.flags());

    assertValid("abcGCD");
    assertInvalid("aBcK");
  }

}
