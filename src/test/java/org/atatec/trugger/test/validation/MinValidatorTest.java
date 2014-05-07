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

import org.atatec.trugger.util.mock.AnnotationMock;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.Min;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimarães
 */
public class MinValidatorTest extends BaseValidatorTest {

  @Test
  public void testMinValidator() {
    Min constraint = new AnnotationMock<Min>() {{
      map(10.0).to(annotation.value());
      map(true).to(annotation.inclusive());
    }}.createMock();
    Validator validator = factory.create(constraint);

    assertTrue(validator.isValid(10));
    assertFalse(validator.isValid(9));
    assertTrue(validator.isValid(10.001));

    constraint = new AnnotationMock<Min>() {{
      map(-10.0).to(annotation.value());
      map(false).to(annotation.inclusive());
    }}.createMock();
    validator = factory.create(constraint);

    assertFalse(validator.isValid(-10));
    assertTrue(validator.isValid(9));
    assertFalse(validator.isValid(-10.001));
    assertFalse(validator.isValid(-14));
  }

  @Test
  public void testTypes() {
    Min constraint = new AnnotationMock<Min>() {{
      map(2.0).to(annotation.value());
      map(true).to(annotation.inclusive());
    }}.createMock();
    Validator validator = factory.create(constraint);

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

    assertTrue(validator.isValid(null));

    assertTrue(validator.isValid(validArray));
    assertTrue(validator.isValid(validList));
    assertTrue(validator.isValid(validMap));
    assertTrue(validator.isValid(validPrimitiveArray));
    assertTrue(validator.isValid(validString));

    assertFalse(validator.isValid(invalidArray));
    assertFalse(validator.isValid(invalidList));
    assertFalse(validator.isValid(invalidMap));
    assertFalse(validator.isValid(invalidPrimitiveArray));
    assertFalse(validator.isValid(invalidString));
  }

  @Test
  public void testDelta() {
    Min constraint = new AnnotationMock<Min>() {{
      map(2.0).to(annotation.value());
      map(1.0).to(annotation.delta());
      map(true).to(annotation.inclusive());
    }}.createMock();
    Validator validator = factory.create(constraint);

    assertTrue(validator.isValid(new Object[1]));
    assertFalse(validator.isValid(new Object[0]));
  }

}
