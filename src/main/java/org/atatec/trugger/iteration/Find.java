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

package org.atatec.trugger.iteration;

import org.atatec.trugger.iteration.impl.FindResult;
import org.atatec.trugger.predicate.Predicate;

/** @author Marcelo Varella Barca Guimarães */
public class Find {

  /**
   * Search for a specific elements using the given predicate. An {@link
   * IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static FindResult the(Predicate predicate) {
    return Iteration.factory.createFindOperation(predicate);
  }

  /**
   * Search for the first specific element using the given predicate. An {@link
   * IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> FindResult first(Predicate<? super E> predicate) {
    return Iteration.factory.createFindFirstOperation(predicate);
  }

  /**
   * Search for all specific elements using the given predicate. An {@link
   * IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> FindAllResult all(Predicate<? super E> predicate) {
    return Iteration.factory.createFindAllOperation(predicate);
  }

}
