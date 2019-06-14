/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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
package io.backpackcloud.trugger.reflection.impl;

import io.backpackcloud.trugger.util.Utils;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A base class for selecting a set of {@link Member} objects.
 *
 * @param <T> The member type.
 * @author Marcelo "Ataxexe" Guimarães
 */
public class MembersSelector<T extends Member> {

  private final MembersFinder<T> finder;
  private final Predicate<? super T> predicate;
  private final Function<Class, Iterable<Class>> function;

  public MembersSelector(MembersFinder<T> finder) {
    this.finder = finder;
    this.predicate = null;
    this.function = Collections::singletonList;
  }

  public MembersSelector(MembersFinder<T> finder,
                         Predicate<? super T> predicate) {
    this.predicate = predicate;
    this.finder = finder;
    this.function = Collections::singletonList;
  }

  public MembersSelector(MembersFinder<T> finder,
                         Predicate<? super T> predicate,
                         Function<Class, Iterable<Class>> function) {
    this.finder = finder;
    this.predicate = predicate;
    this.function = function;
  }

  public final List<T> selectFrom(Object target) {
    final List<T> list = new ArrayList<>();
    for (Class type : function.apply(Utils.resolveType(target))) {
      list.addAll(finder.find(type));
    }
    return applySelection(list);
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
