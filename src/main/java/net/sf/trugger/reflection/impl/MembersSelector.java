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
package net.sf.trugger.reflection.impl;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.Result;
import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.ClassHierarchyIteration;
import net.sf.trugger.util.Utils;

/**
 * A base class for selecting a set of {@link Member} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The member type.
 */
public class MembersSelector<T extends Member> implements Result<Set<T>, Object>, Predicable<T> {

  private final PredicateBuilder<T> builder = new PredicateBuilder<T>(null);
  private final MembersFinder<T> finder;
  private boolean useHierarchy;

  public MembersSelector(MembersFinder<T> finder) {
    this.finder = finder;
  }

  public final Set<T> in(Object target) {
    if (useHierarchy) {
      final Set<T> set = new HashSet<T>();
      new ClassHierarchyIteration() {

        @Override
        protected void iteration(Class<?> clazz) {
          set.addAll(Arrays.asList(finder.find(clazz)));
        }
      }.iterate(target);
      return applySelection(set);
    }
    Class<?> type = Utils.resolveType(target);
    Set<T> set = new HashSet<T>(Arrays.asList(finder.find(type)));
    return applySelection(set);
  }

  private Set<T> applySelection(final Set<T> set) {
    Predicate<T> predicate = builder.predicate();
    if (predicate != null) {
      Iteration.retainFrom(set).elementsMatching(predicate);
    }
    return set;
  }

  /**
   * Indicates that this selector must use the target hierarchy.
   */
  public final void useHierarchy() {
    this.useHierarchy = true;
  }

  /**
   * @return the object for holding the predicates for filtering the result
   */
  public final PredicateBuilder<T> builder() {
    return builder;
  }

  public CompositePredicate<T> toPredicate() {
    CompositePredicate<T> predicate = builder.predicate();
    return predicate == null ? Predicates.<T> alwaysTrue() : predicate;
  }

}
