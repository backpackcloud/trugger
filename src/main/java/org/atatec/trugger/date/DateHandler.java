/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.date;

import java.util.Calendar;
import java.util.Date;

import org.atatec.trugger.loader.ImplementationLoader;

/**
 * An utility class for handling {@link Date} objects.
 *
 * @author Marcelo Guimarães
 * @since 2.0
 */
public class DateHandler {

  private static final DateOperationFactory factory;

  static {
    factory = ImplementationLoader.instance().get(DateOperationFactory.class);
  }

  private DateHandler() {}

  /**
   * Operates the given date.
   *
   * @param date
   *          the date to operate.
   * @return a component for doing the operation.
   */
  public static DateOperation operate(Date date) {
    return factory.createDateOperation(date);
  }

  /**
   * Operates the given time.
   *
   * @param millis
   *          the time in milliseconds.
   * @return a component for doing the operation.
   */
  public static DateOperation operate(long millis) {
    return factory.createDateOperation(millis);
  }

  /**
   * Operates the given calendar.
   *
   * @param calendar
   *          the calendar to operate.
   * @return a component for doing the operation.
   */
  public static DateOperation operate(Calendar calendar) {
    return factory.createDateOperation(calendar);
  }

  /**
   * Returns the current date based on {@link DateType#DATE}.
   *
   * @return the current date.
   */
  public static Date today() {
    Calendar cal = Calendar.getInstance();
    DateType.DATE.clearIrrelevantFields(cal);
    return cal.getTime();
  }

  /**
   * Returns the current date and time based on {@link DateType#DATE_TIME}.
   *
   * @return the current date and time.
   */
  public static Date now() {
    Calendar cal = Calendar.getInstance();
    DateType.DATE_TIME.clearIrrelevantFields(cal);
    return cal.getTime();
  }

  /**
   * Returns the current time based on {@link DateType#TIME}.
   *
   * @return the current time.
   */
  public static Date timeNow() {
    Calendar cal = Calendar.getInstance();
    DateType.TIME.clearIrrelevantFields(cal);
    return cal.getTime();
  }

}
