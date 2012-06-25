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

import java.util.Calendar;

import org.atatec.trugger.date.DateType;
import org.atatec.trugger.validation.validator.Past;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PastValidatorTest extends ValidatorTest<Past> {

  @Test
  public void testIncludeToday() {
    builder.map(true).to(annotation.includeToday());
    builder.map(DateType.DATE).to(annotation.type());
    createValidator();

    validatePastDate(true);
    assertTypeDisallowed(new Object());
    assertTypeDisallowed(Calendar.getInstance());
    assertTypeDisallowed("");
  }

  @Test
  public void testNotIncludeToday() {
    builder.map(false).to(annotation.includeToday());
    builder.map(DateType.DATE).to(annotation.type());

    createValidator();

    validatePastDate(false);
    assertTypeDisallowed(new Object());
    assertTypeDisallowed(Calendar.getInstance());
    assertTypeDisallowed("");
  }

  private void validatePastDate(boolean includeToday) {
    Calendar cal = Calendar.getInstance();
    if (includeToday) {
      assertValid(cal.getTime());
    } else {
      assertInvalid(cal.getTime());
    }
    cal.add(Calendar.DAY_OF_MONTH, 1);
    assertInvalid(cal.getTime());
    cal.add(Calendar.DAY_OF_MONTH, -2);
    assertValid(cal.getTime());
    assertValid(null);

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(cal);
    assertTypeDisallowed("");
  }

}
