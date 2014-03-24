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
  private final Predicate<? super T> predicate;
  private final boolean useHierarchy;

  public MembersSelector(MembersFinder<T> finder) {
    this.finder = finder;
    this.predicate = null;
    this.useHierarchy = false;
  }

  public MembersSelector(MembersFinder<T> finder,
                         Predicate<? super T> predicate) {
    this.predicate = predicate;
    this.finder = finder;
    this.useHierarchy = false;
  }

  public MembersSelector(MembersFinder<T> finder, Predicate<? super T> predicate,
                         boolean useHierarchy) {
    this.finder = finder;
    this.predicate = predicate;
    this.useHierarchy = useHierarchy;
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
    Set<T> set = new HashSet<>(finder.find(type));
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

}
