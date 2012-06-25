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

/**
 * Interface for specifying the unit for a operation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public interface DateOperationUnit {

  /**
   * Operates the seconds.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation second();

  /**
   * Operates the seconds.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation seconds();

  /**
   * Operates the minutes.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation minute();

  /**
   * Operates the minutes.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation minutes();

  /**
   * Operates the hours.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation hour();

  /**
   * Operates the hours.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation hours();

  /**
   * Operates the days.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation day();

  /**
   * Operates the days.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation days();

  /**
   * Operates the weeks.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation week();

  /**
   * Operates the weeks.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation weeks();

  /**
   * Operates the months.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation month();

  /**
   * Operates the months.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation months();

  /**
   * Operates the years.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation year();

  /**
   * Operates the years.
   *
   * @return a reference to the DateOperation.
   */
  DateOperation years();

}
