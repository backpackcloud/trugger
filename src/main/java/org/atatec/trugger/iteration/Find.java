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

/**
 * A class to help searching for elements in a collection.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public class Find {

  /**
   * Finds the only element that matches to the given predicate.
   *
   * @param predicate the predicate to use
   *
   * @return a component for defining the remaining parameters.
   *
   * @throws NonUniqueMatchException if the predicate matches to more than one element.
   */
  public static FindResult the(Predicate predicate) {
    return Iteration.factory.createFindOperation(predicate);
  }

  /**
   * Finds the first element that matches to the given predicate.
   *
   * @param predicate the predicate to use
   *
   * @return a component for defining the remaining parameters.
   */
  public static <E> FindResult first(Predicate<? super E> predicate) {
    return Iteration.factory.createFindFirstOperation(predicate);
  }

  /**
   * Finds all elements that matches to the given predicate.
   *
   * @param predicate the predicate to use
   *
   * @return a component for defining the remaining parameters.
   */
  public static <E> FindAllResult all(Predicate<? super E> predicate) {
    return Iteration.factory.createFindAllOperation(predicate);
  }

}
