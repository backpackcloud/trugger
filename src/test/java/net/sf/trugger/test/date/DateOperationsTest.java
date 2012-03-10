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
package net.sf.trugger.test.date;

import static net.sf.trugger.date.DateHandler.operate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.Date;

import net.sf.trugger.date.DateOperation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class DateOperationsTest {

  private Date date; // 2009-01-10 15h30m

  @Before
  public void initialize() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 10);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.YEAR, 2009);
    calendar.set(Calendar.HOUR_OF_DAY, 15);
    calendar.set(Calendar.MINUTE, 30);
    calendar.set(Calendar.SECOND, 0);

    date = calendar.getTime();
  }

  @After
  public void testDate() {
    assertDate(date, 2009, 1, 10);
    assertTime(date, 15, 30, 0);
  }

  @Test
  public void daysOperationTest() {
    Date result = operate(date.getTime()).add(1).day().result();
    assertDate(result, 2009, 1, 11);

    result = operate(date.getTime()).subtract(2).days().result();
    assertDate(result, 2009, 1, 8);
    assertFalse(date.equals(result));
  }

  @Test
  public void monthsOperationTest() {
    Date result = operate(date).add(1).month().result();
    assertDate(result, 2009, 2, 10);
    assertFalse(date.equals(result));

    result = operate(date).subtract(2).months().result();
    assertDate(result, 2008, 11, 10);
  }

  @Test
  public void yearsOperationTest() {
    Date result = operate(date).add(1).year().result();
    assertDate(result, 2010, 1, 10);

    result = operate(date).subtract(2).years().result();
    assertDate(result, 2007, 1, 10);
  }

  @Test
  public void secondsOperationTest() {
    Date result = operate(date).add(1).second().result();
    assertTime(result, 15, 30, 1);

    result = operate(date).subtract(5).seconds().result();
    assertTime(result, 15, 29, 55);
  }

  @Test
  public void minutesOperationTest() {
    Date result = operate(date).add(1).minute().result();
    assertTime(result, 15, 31, 0);

    result = operate(date).subtract(5).minutes().result();
    assertTime(result, 15, 25, 0);
  }

  @Test
  public void hoursOperationTest() {
    Date result = operate(date).add(1).hour().result();
    assertTime(result, 16, 30, 0);

    result = operate(date).subtract(5).hours().result();
    assertTime(result, 10, 30, 0);
  }

  @Test
  public void weeksOperationTest() {
    Date result = operate(date).add(1).week().result();
    assertDate(result, 2009, 1, 17);

    result = operate(date).subtract(2).weeks().result();
    assertDate(result, 2008, 12, 27);
  }

  @Test
  public void testCalendarOperation() {
    Calendar calendar = Calendar.getInstance();
    DateOperation operation = operate(calendar).add(2).days().subtract(4).years();
    //operations with a calendar will alter the given calendar
    assertEquals(calendar.getTime(), operation.result());
  }

  private void assertDate(Date date, int year, int month, int day) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    assertEquals(year, cal.get(Calendar.YEAR));
    assertEquals(month - 1, cal.get(Calendar.MONTH));
    assertEquals(day, cal.get(Calendar.DAY_OF_MONTH));
  }

  private void assertTime(Date date, int hour, int minute, int second) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute, cal.get(Calendar.MINUTE));
    assertEquals(second, cal.get(Calendar.SECOND));
  }

}
