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

import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.NotEmpty;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimarães
 */
public class NotEmptyValidatorTest extends BaseValidatorTest {

  @Test
  public void testNotEmptyValidator() {
    Validator validator = validatorFor(NotEmpty.class);

    assertTrue(validator.isValid(" "));  // strings are not trimmed
    assertTrue(validator.isValid("non empty"));
    assertTrue(validator.isValid(null)); // this is for NotNull to check

    assertFalse(validator.isValid(""));

    assertTrue(validator.isValid(new int[]{1, 2, 3}));
    assertFalse(validator.isValid(new int[0]));

    assertTrue(validator.isValid(Arrays.asList(0, 1, 2, 3)));
    assertFalse(validator.isValid(Collections.emptyList()));

    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    assertTrue(validator.isValid(map));
    assertFalse(validator.isValid(Collections.emptyMap()));
  }

}
