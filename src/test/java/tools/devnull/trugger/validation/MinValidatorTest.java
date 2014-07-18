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

package tools.devnull.trugger.validation;

import org.junit.Test;
import tools.devnull.trugger.validation.validator.Min;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author Marcelo Guimarães
 */
public class MinValidatorTest extends BaseValidatorTest<Min> {

  @Test
  public void testPositiveValue() {
    when(constraint.value()).thenReturn(10.0);

    assertValid(10);
    assertInvalid(9);
    assertValid(10.001);
  }

  @Test
  public void testNegativeValue() {
    when(constraint.value()).thenReturn(-10.0);

    assertValid(-10);
    assertValid(9);
    assertInvalid(-10.001);
    assertInvalid(-14);
  }

  @Test
  public void testTypes() {
    when(constraint.value()).thenReturn(2.0);

    Object[] validArray = new Object[2];
    Object[] invalidArray = new Object[1];

    Map validMap = new HashMap() {{
      put(0, 1);
      put(1, 1);
    }};
    Map invalidMap = new HashMap() {{
      put(0, 1);
    }};

    List validList = Arrays.asList(1, 2);
    List invalidList = Arrays.asList(1);

    int[] validPrimitiveArray = new int[2];
    int[] invalidPrimitiveArray = new int[0];

    String validString = "abc";
    String invalidString = "";

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
    when(constraint.value()).thenReturn(2.0);
    when(constraint.delta()).thenReturn(1.0);

    assertValid(new Object[1]);
    assertInvalid(new Object[0]);
  }

  @Test
  public void testInclusive() {
    when(constraint.value()).thenReturn(2.0);
    when(constraint.inclusive()).thenReturn(false);

    assertValid(new Object[3]);
    assertInvalid(new Object[2]);
  }

}
