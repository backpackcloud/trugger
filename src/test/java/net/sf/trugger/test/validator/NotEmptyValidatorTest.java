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

import java.util.Arrays;
import java.util.Collections;

import net.sf.trugger.validation.validator.NotEmpty;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NotEmptyValidatorTest extends ValidatorTest<NotEmpty> {

  @Test
  public void testValidator() {
    createValidator();

    assertInvalid(new StringBuilder());
    assertInvalid(new StringBuilder(" "));
    assertInvalid(" ");
    assertInvalid(null);
    assertValid("A");
    assertValid(new StringBuilder("Test"));
    assertInvalid(new Object[0]);
    assertValid(Arrays.asList(1, 2, 4));
    assertInvalid(Collections.EMPTY_LIST);
    assertInvalid(Collections.EMPTY_SET);
    assertInvalid(Collections.EMPTY_MAP);

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(Math.PI);
  }

}
