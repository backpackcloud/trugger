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

import static org.atatec.trugger.date.DateHandler.operate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.atatec.trugger.date.DateType;
import org.atatec.trugger.validation.validator.Before;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class BeforeValidatorTest extends ValidatorTest<Before> {

  @Test
  public void testDateTime() {
    Calendar cal = Calendar.getInstance();
    final Date dateA = cal.getTime();
    operate(cal).add(5).seconds();
    final Date dateB = cal.getTime();
    operate(cal).subtract(10).seconds();
    Date dateC = cal.getTime();

    builder.map(DateType.DATE_TIME).to(annotation.type());
    builder.map(false).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertInvalid(dateB);
    assertInvalid(dateA);
    assertValid(dateC);
    assertValid(null);
  }

  @Test
  public void testDateTimeWithEqual() {
    Calendar cal = Calendar.getInstance();
    final Date dateA = cal.getTime();
    operate(cal).add(5).seconds();
    final Date dateB = cal.getTime();
    operate(cal).subtract(10).seconds();
    Date dateC = cal.getTime();

    builder.map(DateType.DATE_TIME).to(annotation.type());
    builder.map(true).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertInvalid(dateB);
    assertValid(dateA);
    assertValid(dateC);
    assertValid(null);
  }

  @Test
  public void testDate() {
    Calendar cal = Calendar.getInstance();
    cal.set(2009, 5, 5, 5, 5, 5);
    final Date dateA = cal.getTime();
    operate(cal).add(5).days();
    final Date dateB = cal.getTime();
    operate(cal).subtract(5).days().subtract(10).seconds();
    Date dateC = cal.getTime();

    builder.map(DateType.DATE).to(annotation.type());
    builder.map(false).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertInvalid(dateB);
    assertInvalid(dateA);
    assertInvalid(dateC);
    assertValid(operate(dateA).subtract(1).day().result());
    assertValid(null);

    operate(cal).subtract(2).days();
    assertValid(cal.getTime());
  }

  @Test
  public void testDateWithEqual() {
    Calendar cal = Calendar.getInstance();
    cal.set(2009, 5, 5, 5, 5, 5);
    final Date dateA = cal.getTime();
    operate(cal).add(5).days();
    final Date dateB = cal.getTime();
    operate(cal).subtract(5).days().subtract(10).seconds();
    Date dateC = cal.getTime();

    builder.map(DateType.DATE).to(annotation.type());
    builder.map(true).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertInvalid(dateB);
    assertValid(dateA);
    assertValid(dateC);
    assertValid(operate(dateA).subtract(2).days().result());
    assertValid(null);

    operate(cal).subtract(2).days();
    assertValid(cal.getTime());
  }

  @Test
  public void testWithInvalidReference() {
    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", null);
    }};

    builder.map(DateType.DATE_TIME).to(annotation.type());
    builder.map(true).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    createValidator(target);

    assertValid(new Date());
    assertValid(null);
  }

  @Test
  public void testTime() {
    Calendar cal = Calendar.getInstance();
    final Date dateA = cal.getTime();

    builder.map(DateType.TIME).to(annotation.type());
    builder.map(false).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertInvalid(dateA);
    assertInvalid(operate(dateA).add(1).second().result());
    assertValid(operate(dateA).subtract(1).second().add(10).days().result());
    assertValid(operate(dateA).subtract(2).minutes().result());
    assertValid(null);
  }

  @Test
  public void testTimeWithEquals() {
    Calendar cal = Calendar.getInstance();
    final Date dateA = cal.getTime();

    builder.map(DateType.TIME).to(annotation.type());
    builder.map(true).to(annotation.validIfEquals());
    builder.map("dateA").to(annotation.reference());

    Map<String, Object> target = new HashMap<String, Object>(){{
      put("dateA", dateA);
    }};
    createValidator(target);

    assertValid(dateA);
    assertInvalid(operate(dateA).add(1).second().result());
    assertValid(operate(dateA).add(10).days().result());
    assertValid(operate(dateA).subtract(2).minutes().result());
    assertValid(null);
  }

}
