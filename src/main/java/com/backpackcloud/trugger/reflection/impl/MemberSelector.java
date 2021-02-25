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
package com.backpackcloud.trugger.reflection.impl;

import com.backpackcloud.trugger.reflection.ReflectionException;
import com.backpackcloud.trugger.util.Utils;

import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A base class for selecting a single {@link Member} object.
 *
 * @param <T> The member type.
 * @author Marcelo Guimaraes
 */
public class MemberSelector<T extends Member> {

  private final MemberFinder<T> finder;
  private final Predicate<? super T> predicate;
  private final Function<Class, Iterable<Class>> function;

  public MemberSelector(MemberFinder<T> finder,
                        Predicate<? super T> predicate,
                        Function<Class, Iterable<Class>> function) {
    this.finder = finder;
    this.predicate = predicate;
    this.function = function;
  }

  public MemberSelector(MemberFinder<T> finder, Predicate<? super T> predicate) {
    this(finder, predicate, Collections::singletonList);
  }

  private T findMember(Class<?> type) {
    T element;
    try {
      element = finder.find(type);
      if (element != null) {
        if (predicate != null) {
          element = predicate.test(element) ? element : null;
        }
      }
    } catch (NoSuchMethodException | NoSuchFieldException e) {
      return null;
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
    return element;
  }

  public final Optional<T> selectFrom(Object target) {
    for (Class type : function.apply(Utils.resolveType(target))) {
      T member = findMember(type);
      if (member != null) {
        return Optional.of(member);
      }
    }
    return Optional.empty();
  }

}
