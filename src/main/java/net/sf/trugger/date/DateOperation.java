/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
import java.util.Date;

/**
 * Interface that represents an operation over a date.
 * <p>
 * The operations may be chained, example:
 *
 * <pre>
 * Date result = dateOperation.add(1).day().add(30).minutes().result();
 * </pre>
 *
 * Note that if the object is a {@link Calendar}, it will be affected by the
 * operation. So, the {@link #result() result} will be the given calendar.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public interface DateOperation {

  /**
   * Adds a quantity to the date.
   *
   * @param quantity
   *          the quantity to add.
   * @return a component to specify the quantity unit.
   */
  DateOperationUnit add(int quantity);

  /**
   * Subtracts a quantity to the date.
   *
   * @param quantity
   *          the quantity to subtract.
   * @return a component to specify the quantity unit.
   */
  DateOperationUnit subtract(int quantity);

  /**
   * Retrieves the operation result.
   *
   * @return the operation result.
   */
  Date result();

}
