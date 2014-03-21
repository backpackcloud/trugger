/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.Result;
import org.atatec.trugger.util.Utils;

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.atatec.trugger.reflection.Reflection.hierarchyOf;

/**
 * A base class for selecting a set of {@link Member} objects.
 *
 * @param <T> The member type.
 * @author Marcelo Guimarães
 */
public class MembersSelector<T extends Member> implements Result<Set<T>, Object> {

  private final MembersFinder<T> finder;
  private Predicate<T> predicate;
  private boolean useHierarchy;

  public MembersSelector(MembersFinder<T> finder) {
    this.finder = finder;
  }

  public final Set<T> in(Object target) {
    if (useHierarchy) {
      final Set<T> set = new HashSet<T>();
      for (Class type : hierarchyOf(target)) {
        set.addAll(finder.find(type));
      }
      return applySelection(set);
    }
    Class<?> type = Utils.resolveType(target);
    Set<T> set = new HashSet<T>(finder.find(type));
    return applySelection(set);
  }

  private Set<T> applySelection(final Set<T> set) {
    if (predicate != null) {
      return set.stream()
        .filter(predicate)
        .collect(Collectors.toSet());
    }
    return set;
  }

  /**
   * Indicates that this selector must use the target hierarchy.
   */
  public final void useHierarchy() {
    this.useHierarchy = true;
  }

  public void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

}
