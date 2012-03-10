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
package net.sf.trugger.predicate;

/**
 * Helper class for building composite predicates using the AND operation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The element type.
 */
public class PredicateBuilder<T> {

  private final CompositePredicate<T> defaultPredicate;

  private CompositePredicate<T> predicate;

  /**
   * Creates a new builder using a {@link Predicates#ALWAYS_TRUE} as the default
   * predicate.
   */
  public PredicateBuilder() {
    this(Predicates.<T> alwaysTrue());
  }

  /**
   * Creates a new builder using the given predicate as the default one.
   *
   * @param defaultPredicate
   *          the default predicate, may be <code>null</code>.
   */
  public PredicateBuilder(CompositePredicate<T> defaultPredicate) {
    this.defaultPredicate = defaultPredicate;
  }

  /**
   * Adds the given predicate.
   *
   * @param predicate
   *          the predicate to add.
   */
  public PredicateBuilder<T> add(Predicate<? super T> predicate) {
    this.predicate =
        (this.predicate == null ? Predicates.newComposition(predicate) : this.predicate.and(predicate));
    return this;
  }

  /**
   * Returns the created predicate. This predicate will return <code>true</code>
   * if all the {@link #add(Predicate) added} predicates returns
   * <code>true</code>.
   * <p>
   * Note that this method can return <code>null</code> if <code>null</code> is
   * defined as the default predicate.
   *
   * @return the created predicate, or the default if no one is added to this
   *         builder.
   */
  public CompositePredicate<T> predicate() {
    return predicate == null ? defaultPredicate : predicate;
  }

}
