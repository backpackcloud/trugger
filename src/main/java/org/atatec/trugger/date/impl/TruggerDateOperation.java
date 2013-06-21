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
package org.atatec.trugger.date.impl;

import java.util.Calendar;
import java.util.Date;

import org.atatec.trugger.date.DateOperation;
import org.atatec.trugger.date.DateOperationUnit;

/**
 * A class to operate dates.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerDateOperation implements DateOperation {

  private final Calendar calendar;

  /**
   * @param millis
   *          the time in millis to operate.
   */
  public TruggerDateOperation(long millis) {
    this.calendar = Calendar.getInstance();
    this.calendar.setTimeInMillis(millis);
  }

  /**
   * @param date
   *          the date to operate
   */
  public TruggerDateOperation(Date date) {
    this(date.getTime());
  }

  /**
   * @param calendar
   *          the calendar to operate.
   */
  public TruggerDateOperation(Calendar calendar) {
    this.calendar = calendar;
  }

  @Override
  public DateOperationUnit add(int quantity) {
    return new TruggerDateOperationUnit(quantity);
  }

  @Override
  public DateOperationUnit subtract(int quantity) {
    return new TruggerDateOperationUnit(-quantity);
  }

  @Override
  public Date result() {
    return calendar.getTime();
  }

  /**
   * A class for selecting the unit for the operation.
   */
  private class TruggerDateOperationUnit implements DateOperationUnit {

    private final int quantity;

    private TruggerDateOperationUnit(int quantity) {
      this.quantity = quantity;
    }

    private DateOperation operate(int calendarField) {
      calendar.add(calendarField, quantity);
      return TruggerDateOperation.this;
    }

    @Override
    public DateOperation day() {
      return operate(Calendar.DAY_OF_MONTH);
    }

    @Override
    public DateOperation days() {
      return operate(Calendar.DAY_OF_MONTH);
    }

    @Override
    public DateOperation hour() {
      return operate(Calendar.HOUR_OF_DAY);
    }

    @Override
    public DateOperation hours() {
      return operate(Calendar.HOUR_OF_DAY);
    }

    @Override
    public DateOperation minute() {
      return operate(Calendar.MINUTE);
    }

    @Override
    public DateOperation minutes() {
      return operate(Calendar.MINUTE);
    }

    @Override
    public DateOperation month() {
      return operate(Calendar.MONTH);
    }

    @Override
    public DateOperation months() {
      return operate(Calendar.MONTH);
    }

    @Override
    public DateOperation second() {
      return operate(Calendar.SECOND);
    }

    @Override
    public DateOperation seconds() {
      return operate(Calendar.SECOND);
    }

    @Override
    public DateOperation week() {
      return operate(Calendar.WEEK_OF_MONTH);
    }

    @Override
    public DateOperation weeks() {
      return operate(Calendar.WEEK_OF_MONTH);
    }

    @Override
    public DateOperation year() {
      return operate(Calendar.YEAR);
    }

    @Override
    public DateOperation years() {
      return operate(Calendar.YEAR);
    }

  }

}
