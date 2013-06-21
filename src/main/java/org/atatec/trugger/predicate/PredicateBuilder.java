/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.predicate;

import org.atatec.trugger.iteration.Find;
import org.atatec.trugger.iteration.NonUniqueMatchException;

import java.util.Collection;

/**
 * Helper class for building composite predicates using the AND operation.
 *
 * @param <T> The element type.
 *
 * @author Marcelo Guimarães
 */
public class PredicateBuilder<T> {

  private CompositePredicate<T> predicate;

  /**
   * Adds the given predicate.
   *
   * @param other the predicate to add.
   */
  public PredicateBuilder<T> add(Predicate other) {
    predicate = predicate == null ? Predicates.wrap(other) : predicate.and(other);
    return this;
  }

  /**
   * Returns the created predicate. This predicate will return <code>true</code> if all
   * the {@link #add(Predicate) added} predicates returns <code>true</code>.
   *
   * @return the created predicate.
   */
  public CompositePredicate<T> predicate() {
    return predicate;
  }

  /**
   * Tries to find an element matching the build predicate
   *
   * @param collection the collection to search
   *
   * @return the found element or <code>null</code> if no one matches.
   *
   * @throws NonUniqueMatchException if the predicate matches to more than one element
   */
  public T findIn(Collection<T> collection) {
    if (collection.isEmpty()) {
      return null;
    }
    CompositePredicate<T> selectedElement = predicate();
    if (selectedElement != null) {
      return Find.the(selectedElement).in(collection);
    }
    if (collection.size() == 1) {
      return collection.iterator().next();
    }
    throw new NonUniqueMatchException();
  }

}
