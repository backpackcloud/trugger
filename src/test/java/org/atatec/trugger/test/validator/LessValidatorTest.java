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

import java.util.HashMap;
import java.util.Map;

import org.atatec.trugger.validation.validator.Less;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class LessValidatorTest extends ValidatorTest<Less> {

  @Test
  public void testNotEqual() {
    Map<String, Object> target = new HashMap<String, Object>(){{
      put("value", 5);
    }};
    builder.map("value").to(annotation.than());
    createValidator(target);

    assertInvalid(6);
    assertInvalid(5);
    assertValid(4);
    assertInvalid(10);

    assertTypeDisallowed(new Object());
    assertValid(null);
  }

  @Test
  public void testEqual() {
    Map<String, Object> target = new HashMap<String, Object>(){{
      put("value", 5);
    }};
    builder.map("value").to(annotation.than());
    builder.map(true).to(annotation.orEqual());
    createValidator(target);

    assertInvalid(6);
    assertValid(5);
    assertValid(4);
    assertInvalid(10);

    assertTypeDisallowed(new Object());
    assertValid(null);
  }

}
