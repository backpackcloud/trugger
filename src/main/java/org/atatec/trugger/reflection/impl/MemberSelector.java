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
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.util.Utils;

import java.lang.reflect.Member;
import java.util.function.Predicate;

/**
 * A base class for selecting a single {@link Member} object.
 *
 * @param <T> The member type.
 *
 * @author Marcelo Guimarães
 */
public class MemberSelector<T extends Member> implements Result<T,Object> {

  private final MemberFinder<T> finder;
  private final Predicate<? super T> predicate;
  private final boolean useHierarchy;

  public MemberSelector(MemberFinder<T> finder, Predicate<? super T> predicate,
                        boolean useHierarchy) {
    this.finder = finder;
    this.predicate = predicate;
    this.useHierarchy = useHierarchy;
  }

  public MemberSelector(MemberFinder<T> finder, Predicate<? super T> predicate) {
    this(finder, predicate, false);
  }

  private T findMember(Class<?> type) {
    try {
      T element = finder.find(type);
      if (element != null) {
        if (predicate != null) {
          return predicate.test(element) ? element : null;
        }
        return element;
      }
      return null;
    } catch (NoSuchMethodException e) {
      return null;
    } catch (NoSuchFieldException e) {
      return null;
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  public final T in(Object target) {
    if (useHierarchy) {
      for (Class type : Reflection.hierarchyOf(target)) {
        T member = findMember(type);
        if (member != null) {
          return member;
        }
      }
      return null;
    }
    Class<?> type = Utils.resolveType(target);
    return findMember(type);
  }

}
