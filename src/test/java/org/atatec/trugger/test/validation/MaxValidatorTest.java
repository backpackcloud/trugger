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

import org.atatec.trugger.validation.validator.Max;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcelo Guimarães
 */
public class MaxValidatorTest extends BaseValidatorTest<Max> {

  @Test
  public void testPositiveValue() {
    map(10.0).to(constraint.value());
    map(true).to(constraint.inclusive());

    assertValid(null);

    assertValid(10);
    assertValid(9);
    assertInvalid(10.001);
  }

  @Test
  public void testNegativeValue() {
    map(-10.0).to(constraint.value());

    assertValid(-10);
    assertInvalid(9);
    assertValid(-10.001);
    assertValid(-14);
  }

  @Test
  public void testTypes() {
    map(2.0).to(constraint.value());

    Object[] validArray = new Object[2];
    Object[] invalidArray = new Object[3];

    Map validMap = new HashMap() {{
      put(0, 1);
      put(1, 1);
    }};
    Map invalidMap = new HashMap() {{
      put(0, 1);
      put(1, 1);
      put(2, 1);
    }};

    List validList = Arrays.asList(1, 2);
    List invalidList = Arrays.asList(1, 2, 3);

    int[] validPrimitiveArray = new int[2];
    int[] invalidPrimitiveArray = new int[3];

    String validString = "";
    String invalidString = "abc";

    assertValid(null);

    assertValid(validArray);
    assertValid(validList);
    assertValid(validMap);
    assertValid(validPrimitiveArray);
    assertValid(validString);

    assertInvalid(invalidArray);
    assertInvalid(invalidList);
    assertInvalid(invalidMap);
    assertInvalid(invalidPrimitiveArray);
    assertInvalid(invalidString);
  }

  @Test
  public void testDelta() {
    map(2.0).to(constraint.value());
    map(1.0).to(constraint.delta());

    assertValid(new Object[3]);
    assertInvalid(new Object[4]);
  }

  @Test
  public void testInclusive() {
    map(2.0).to(constraint.value());
    map(false).to(constraint.inclusive());

    assertValid(new Object[1]);
    assertInvalid(new Object[2]);
  }

}
