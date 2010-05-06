/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.date;

import java.util.Calendar;

/**
 * Indicates what part of the date object is relevant.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public enum DateType {

  /**
   * Only the date is relevant (year, month and day).
   */
  DATE {

    @Override
    public void clearIrrelevantFields(Calendar cal) {
      super.clearIrrelevantFields(cal);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.clear(Calendar.MINUTE);
      cal.clear(Calendar.SECOND);
    }
  },
  /**
   * Only the time is relevant (hour, minute and second).
   */
  TIME {

    @Override
    public void clearIrrelevantFields(Calendar cal) {
      super.clearIrrelevantFields(cal);
      cal.clear(Calendar.YEAR);
      cal.clear(Calendar.MONTH);
      cal.set(Calendar.DATE, 1);
    }
  },
  /**
   * Both date (year, month and day) and time (hour, minute and second) are
   * relevant.
   */
  DATE_TIME;

  /**
   * Clears the fields of the given calendar that are irrelevant for this type.
   *
   * @param cal
   *          the calendar for cleaning the fields.
   */
  public void clearIrrelevantFields(Calendar cal) {
    cal.clear(Calendar.MILLISECOND);
  }

}
