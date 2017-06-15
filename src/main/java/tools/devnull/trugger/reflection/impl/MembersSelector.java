/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.Result;
import tools.devnull.trugger.util.Utils;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.Reflection.hierarchyOf;

/**
 * A base class for selecting a set of {@link Member} objects.
 *
 * @param <T> The member type.
 * @author Marcelo Guimarães
 */
public class MembersSelector<T extends Member>
    implements Result<List<T>, Object> {

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

  public final List<T> in(Object target) {
    if (useHierarchy) {
      final List<T> list = new ArrayList<>();
      for (Class type : hierarchyOf(target)) {
        list.addAll(finder.find(type));
      }
      return applySelection(list);
    }
    Class<?> type = Utils.resolveType(target);
    return applySelection(finder.find(type));
  }

  private List<T> applySelection(final List<T> list) {
    if (predicate != null) {
      return list.stream()
          .filter(predicate)
          .collect(Collectors.toList());
    }
    return list;
  }

}
