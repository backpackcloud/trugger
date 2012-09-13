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

import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;

/**
 * An utility class for making iterations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.4
 */
public final class Iteration {

  static final IterationFactory factory;

  private Iteration() {
  }

  static {
    factory = ImplementationLoader.instance().get(IterationFactory.class);
  }

  /**
   * Copy the collection elements that matches with the given conditions to another
   * collection.
   * <p/>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SourceSelector copy(Predicate<? super E> predicate) {
    return factory.createCopyOperation(predicate);
  }

  public static <E> SourceSelector copy() {
    return factory.createCopyOperation(Predicates.ALWAYS_TRUE);
  }

  /**
   * Move the collection elements that matches with the given conditions to another
   * collection.
   * <p/>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> SourceSelector move(Predicate<? super E> predicate) {
    return factory.createMoveOperation(predicate);
  }

  public static <E> SourceSelector move() {
    return factory.createMoveOperation(Predicates.ALWAYS_TRUE);
  }

  /**
   * Retains the elements that matches with the given conditions.
   * <p/>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> IterationSourceSelector retain(Predicate<? super E> predicate) {
    return factory.createRetainOperation(predicate);
  }

  /**
   * Removes the elements that matches with the given conditions.
   * <p/>
   * A {@link IterationFactory} is used to create the component.
   *
   * @return the component for using.
   */
  public static <E> IterationSourceSelector remove(Predicate<? super E> predicate) {
    return factory.createRemoveOperation(predicate);
  }

}
