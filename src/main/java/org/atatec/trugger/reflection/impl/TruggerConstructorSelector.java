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
package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.iteration.SearchException;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.reflection.ConstructorPredicates;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ConstructorSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

import static org.atatec.trugger.iteration.Iteration.selectFrom;

/**
 * A default implementation for the constructor selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerConstructorSelector implements ConstructorSelector {

  private PredicateBuilder<Constructor<?>> builder = new PredicateBuilder<Constructor<?>>();

  private Class[] parameterTypes = null;

  private MemberFindersRegistry registry;

  public TruggerConstructorSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public ConstructorSelector that(Predicate<? super Constructor<?>> predicate) {
    builder.add(predicate);
    return this;
  }

  public ConstructorSelector withParameters(Class<?>... parameterTypes) {
    this.parameterTypes = parameterTypes;
    return this;
  }

  @Override
  public Constructor<?> in(Object target) throws ReflectionException {
    if (parameterTypes != null) {
      return (Constructor<?>) new MemberSelector(registry.constructorFinder(parameterTypes), builder.predicate()).in(target);
    }
    MembersSelector<Constructor<?>> selector = new MembersSelector<Constructor<?>>(registry.constructorsFinder());
    Set<Constructor<?>> set = selector.in(target);
    try {
      return selectFrom(set).element(builder.predicate());
    } catch (SearchException e) {
      throw new ReflectionException(e);
    }
  }

  public ConstructorSelector withoutParameters() {
    return withParameters();
  }

  public ConstructorSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public ConstructorSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public ConstructorSelector annotated() {
    builder.add(ReflectionPredicates.IS_ANNOTATED);
    return this;
  }

  public ConstructorSelector notAnnotated() {
    builder.add(ReflectionPredicates.IS_NOT_ANNOTATED);
    return this;
  }

  public CompositePredicate<Constructor<?>> toPredicate() {
    CompositePredicate<Constructor<?>> predicate = builder.predicate();
    if (parameterTypes != null) {
      return predicate.and(ConstructorPredicates.takes(parameterTypes));
    }
    return predicate;
  }

}
