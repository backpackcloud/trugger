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
package net.sf.trugger.element;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;

/**
 * A value handler for {@link Element} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public interface ElementValueHandler extends ValueHandler {

  /**
   * @return the element value formatted for displaying.
   * @throws HandlingException
   *           if anything go wrong.
   */
  String formattedValue() throws HandlingException;

  /**
   * Sets the value by parsing the formatted value given.
   *
   * @param value
   * @throws HandlingException
   *           if anything go wrong.
   */
  void formattedValue(String value) throws HandlingException;

}
