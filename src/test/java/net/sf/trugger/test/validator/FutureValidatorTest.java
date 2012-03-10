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
package net.sf.trugger.test.validator;

import java.util.Calendar;

import net.sf.trugger.date.DateType;
import net.sf.trugger.validation.validator.Future;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FutureValidatorTest extends ValidatorTest<Future> {

  @Test
  public void testValidatorNotIncludingToday() {
    builder.map(false).to(annotation.includeToday());
    builder.map(DateType.DATE).to(annotation.type());
    createValidator();
    validateFutureDate(false);

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(Calendar.getInstance());
    assertTypeDisallowed("");
  }

  @Test
  public void testValidatorIncludingToday() {
    builder.map(true).to(annotation.includeToday());
    builder.map(DateType.DATE).to(annotation.type());
    createValidator();
    validateFutureDate(true);

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(Calendar.getInstance());
    assertTypeDisallowed("");
  }

  private void validateFutureDate(boolean includeToday) {
    Calendar cal = Calendar.getInstance();
    if (includeToday) {
      assertValid(cal.getTime());
    } else {
      assertInvalid(cal.getTime());
    }
    cal.add(Calendar.DAY_OF_MONTH, 1);
    assertValid(cal.getTime());
    cal.add(Calendar.DAY_OF_MONTH, -2);
    assertInvalid(cal.getTime());
  }

}
