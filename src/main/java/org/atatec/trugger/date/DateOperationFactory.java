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
package org.atatec.trugger.date;

import java.util.Calendar;
import java.util.Date;

/**
 * A factory for creating components to operate dates.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public interface DateOperationFactory {

  /**
   * Creates a component for operating the given date.
   *
   * @param date
   *          the date to operate.
   * @return a component for doing the operation.
   */
  DateOperation createDateOperation(Date date);

  /**
   * Creates a component for operating the given time in milliseconds.
   *
   * @param millis
   *          the time in milliseconds to operate.
   * @return a component for doing the operation.
   */
  DateOperation createDateOperation(long millis);

  /**
   * Creates a component for operating the given calendar.
   * <p>
   * Note that the operation will affect the given calendar.
   *
   * @param calendar
   *          the calendar to operate.
   * @return a component for doing the operation.
   */
  DateOperation createDateOperation(Calendar calendar);

}
