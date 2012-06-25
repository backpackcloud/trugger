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

import org.atatec.trugger.predicate.Predicate;

/**
 * Base interface for iterations that uses a source and a destination.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The type of the elements in the source collection.
 * @since 2.4
 */
public interface SrcToDestIteration<E> {

  /**
   * Executes the operation for the elements that matches with the given
   * predicate.
   *
   * @param predicate
   *          the predicate for matching
   * @return the component for selecting the source collection for the
   *         operation.
   */
  IterationSourceSelector<E> elements(Predicate<? super E> predicate);

  /**
   * Executes the operation for all elements.
   *
   * @return the component for selecting the source collection.
   */
  IterationSourceSelector<E> all();

}
