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
import org.atatec.trugger.date.DateOperationFactory;

/**
 * The default factory to create date operations.
 *
 * @author Marcelo Guimarães
 */
public class TruggerDateOperationFactory implements DateOperationFactory {

  /**
   * Returns a new {@link TruggerDateOperation}.
   */
  @Override
  public DateOperation createDateOperation(Calendar calendar) {
    return new TruggerDateOperation(calendar);
  }

  /**
   * Returns a new {@link TruggerDateOperation}.
   */
  @Override
  public DateOperation createDateOperation(Date date) {
    return new TruggerDateOperation(date);
  }

  /**
   * Returns a new {@link TruggerDateOperation}.
   */
  @Override
  public DateOperation createDateOperation(long millis) {
    return new TruggerDateOperation(millis);
  }

}
