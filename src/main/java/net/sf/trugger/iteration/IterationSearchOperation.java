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
package net.sf.trugger.iteration;

import net.sf.trugger.predicate.Predicate;

import java.util.List;

/**
 * Interface that defines a search operation for a Collection.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The element type.
 * @since 2.4
 */
public interface IterationSearchOperation<E> {

  /**
   * Selects a single element that matches with the given predicate.
   * <p>
   * If more than one element matches, an exception will be thrown.
   *
   * @param predicate
   *          the predicate to match.
   * @return the element found.
   * @throws SearchException
   *           if more than one element is found.
   */
  E element(Predicate<? super E> predicate) throws SearchException;

  /**
   * Selects the first element that matches with the given predicate.
   *
   * @param predicate
   *          the predicate to match.
   * @return the element found.
   */
  E first(Predicate<? super E> predicate);

  /**
   * Selects a list of elements that matches with the given predicate.
   *
   * @param predicate
   *          the predicate to match.
   * @return a list with the elements found.
   */
  List<E> elements(Predicate<? super E> predicate);

}
