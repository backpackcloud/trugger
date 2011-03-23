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

import net.sf.trugger.loader.ImplementationLoader;

/**
 * An utility class for making iterations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.4
 */
public final class Iteration {

  private static final IterationFactory factory;

  private Iteration() {}

  static {
    factory = ImplementationLoader.getInstance().get(IterationFactory.class);
  }

  /**
   * Retains the collection elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> retainFrom(Collection<? extends E> collection) {
    return retainFrom(collection.iterator());
  }

  /**
   * Removes the collection elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> removeFrom(Collection<? extends E> collection) {
    return removeFrom(collection.iterator());
  }

  /**
   * Count the collection elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> countIn(Collection<? extends E> collection) {
    return countIn(collection.iterator());
  }

  /**
   * Copy the collection elements that matches with the given conditions to
   * another collection.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> TransformingIteration<E> copyTo(Collection<E> collection) {
    return factory.createCopyOperation(collection);
  }

  /**
   * Move the collection elements that matches with the given conditions to
   * another collection.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> TransformingIteration<E> moveTo(Collection<E> collection) {
    return factory.createMoveOperation(collection);
  }

  /**
   * Search for specific elements in the given collection. A
   * {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> IterationSearchOperation<E> selectFrom(Collection<E> collection) {
    return selectFrom(collection.iterator());
  }

  /**
   * Retains the elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> retainFrom(Iterator<? extends E> iterator) {
    return factory.createRetainOperation(iterator);
  }

  /**
   * Removes the elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> removeFrom(Iterator<? extends E> iterator) {
    return factory.createRemoveOperation(iterator);
  }

  /**
   * Count the elements that matches with the given conditions.
   * <p>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SrcIteration<E> countIn(Iterator<? extends E> iterator) {
    return factory.createCountOperation(iterator);
  }

  /**
   * Search for specific elements using the given iterator. A
   * {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> IterationSearchOperation<E> selectFrom(Iterator<E> iterator) {
    return factory.createSearchOperation(iterator);
  }

}
