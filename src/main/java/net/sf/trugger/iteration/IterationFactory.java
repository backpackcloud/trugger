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
package net.sf.trugger.iteration;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface that defines a factory for iteration operations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.4
 */
public interface IterationFactory {

  /**
   * Creates the implementation for retaining elements based on giving
   * conditions.
   *
   * @return the component for this operation.
   */
  <E> SrcIteration<E> createRetainOperation(Iterator<? extends E> iterator);

  /**
   * Creates the implementation for removing elements based on giving
   * conditions.
   *
   * @return the component for this operation.
   */
  <E> SrcIteration<E> createRemoveOperation(Iterator<? extends E> iterator);

  /**
   * Creates the implementation for counting elements based on giving
   * conditions.
   *
   * @return the component for this operation.
   * @since 1.2
   */
  <E> SrcIteration<E> createCountOperation(Iterator<? extends E> iterator);

  /**
   * Creates the implementation for moving elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  <E> TransformingIteration<E> createMoveOperation(Collection<E> collection);

  /**
   * Creates the implementation for copying elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  <E> TransformingIteration<E> createCopyOperation(Collection<E> collection);

  /**
   * Creates the implementation for searching elements.
   *
   * @return the component for this operation
   */
  <E> IterationSearchOperation<E> createSearchOperation(Iterator<E> iterator);

}
