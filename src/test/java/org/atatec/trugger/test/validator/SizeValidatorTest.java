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

import java.util.Arrays;
import java.util.Collections;

import org.atatec.trugger.validation.validator.Size;

import org.junit.Test;

public class SizeValidatorTest extends ValidatorTest<Size> {

  @Test
  public void testValidator() {
    builder.map(4).to(annotation.min());
    builder.map(10).to(annotation.max());
    createValidator();

    assertValid(new int[] { 1, 2, 3, 4 });
    assertInvalid(Collections.EMPTY_LIST);
    assertInvalid(Collections.EMPTY_MAP);
    assertInvalid(Collections.EMPTY_SET);
    assertInvalid(Arrays.asList(1, 4, 5, 7, 2, 5, 7, 5, 7, 6, 11));
  }

}
