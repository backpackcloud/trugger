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

import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ConstructorSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A default implementation for the constructor selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerConstructorSelector implements ConstructorSelector {

  private Predicate<? super Constructor> predicate;

  private Class[] parameterTypes = null;

  private MemberFindersRegistry registry;

  public TruggerConstructorSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public ConstructorSelector that(Predicate<? super Constructor<?>> predicate) {
    add(predicate);
    return this;
  }

  public ConstructorSelector withParameters(Class<?>... parameterTypes) {
    this.parameterTypes = parameterTypes;
    return this;
  }

  @Override
  public Constructor<?> in(Object target) throws ReflectionException {
    if (parameterTypes != null) {
      return (Constructor<?>)
          new MemberSelector(registry.constructorFinder(parameterTypes), predicate)
              .in(target);
    }
    Set<Constructor<?>> constructors =
        new MembersSelector<>(registry.constructorsFinder()).in(target);
    if (predicate != null) {
      return constructors.stream()
          .filter(predicate)
          .findAny().orElse(null);
    } else if (constructors.size() > 1) {
      throw new ReflectionException("More than one constructor found for " + target.getClass());
    } else {
      return constructors.iterator().next();
    }
  }

  public ConstructorSelector withoutParameters() {
    return withParameters();
  }

  public ConstructorSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public ConstructorSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public ConstructorSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ConstructorSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

}
