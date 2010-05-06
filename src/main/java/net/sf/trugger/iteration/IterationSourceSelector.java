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
package net.sf.trugger.iteration;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface that defines a source for an iteration.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The type of the elements in the collection.
 * @since 2.4
 */
public interface IterationSourceSelector<E> {

  /**
   * Specifies the elements by passing an iterator.
   * <p>
   * After this call, the operation must be executed.
   *
   * @param iterator
   *          an iterator for the source elements.
   * @return the number of elements affected by the operation.
   */
  int from(Iterator<E> iterator);

  /**
   * Specifies the source collection as the elements for iteration.
   * <p>
   * After this call, the operation must be executed.
   *
   * @param collection
   *          the source collection.
   * @return the number of elements affected by the operation.
   */
  int from(Collection<E> collection);

}
