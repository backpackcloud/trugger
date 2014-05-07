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

import org.atatec.trugger.validation.validator.GreaterThan;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.atatec.trugger.test.TruggerTest.element;

/**
 * @author Marcelo Guimarães
 */
public class GreaterThanValidatorTest extends BaseValidatorTest<GreaterThan> {

  @Test
  public void testValidator() {
    Map<String, Integer> map = new HashMap<>();
    map.put("y", 20);

    map("y").to(constraint.value());

    createValidator(
        element().named("x").annotatedWith(constraint).createMock(),
        map
    );

    assertValid(30);
    assertInvalid(20);
    assertInvalid(15);
  }

  @Test
  public void testValidIfEquals() {
    Map<String, Integer> map = new HashMap<>();
    map.put("y", 20);

    map("y").to(constraint.value());
    map(true).to(constraint.orEqual());

    createValidator(
        element().named("x").annotatedWith(constraint).createMock(),
        map
    );

    assertValid(30);
    assertValid(20);
    assertInvalid(15);
  }

  @Test
  public void testNullValue() {
    Map<String, Integer> map = new HashMap<>();
    map.put("y", null);

    map("y").to(constraint.value());

    createValidator(
        element().named("x").annotatedWith(constraint).createMock(),
        map
    );

    assertValid(10);
    assertValid(20);
    assertValid(35);
  }

}
