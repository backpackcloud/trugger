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
 * Interface that defines an iteration over a source.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The element of the types in the collection.
 * @since 2.4
 */
public interface SrcIteration<E> {

  /**
   * Executes the operation for the elements in that matches with the given
   * predicate.
   *
   * @param predicate
   *          the predicate for matching
   * @return the number of elements affected by this operation.
   */
  int anyThat(Predicate<? super E> predicate);

}
