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

}
