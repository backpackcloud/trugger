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

import static org.atatec.trugger.iteration.Iteration.selectFrom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import org.atatec.trugger.iteration.SearchException;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.reflection.Access;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.MethodSelector;

/**
 * A default implementation for the method selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerMethodSelector implements MethodSelector {

  private final PredicateBuilder<Method> builder = new PredicateBuilder<Method>();
  private final String name;
  private boolean recursively;
  private Class[] parameterTypes;

  public TruggerMethodSelector(String name) {
    this.name = name;
  }

  public MethodSelector withAccess(Access access) {
    builder.add(access.memberPredicate());
    return this;
  }

  public MethodSelector nonStatic() {
    builder.add(ReflectionPredicates.NON_STATIC);
    return this;
  }

  public MethodSelector nonFinal() {
    builder.add(ReflectionPredicates.NON_FINAL);
    return this;
  }

  public MethodSelector that(Predicate<? super Method> predicate) {
    builder.add(predicate);
    return this;
  }

  public MethodSelector annotated() {
    builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public MethodSelector notAnnotated() {
    builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public MethodSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.annotatedWith(type));
    return this;
  }

  public MethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }

  public MethodSelector returning(Class<?> returnType) {
    builder.add(ReflectionPredicates.ofReturnType(returnType));
    return this;
  }

  public MethodSelector withoutReturnType() {
    builder.add(ReflectionPredicates.ofReturnType(Void.TYPE));
    return this;
  }

  public MethodSelector withParameters(final Class<?>... parameterTypes) {
    this.parameterTypes = parameterTypes;
    return this;
  }

  public Method in(Object target) {
    if(parameterTypes != null) {
      MethodFinder finder = new MethodFinder(name, parameterTypes);
      final CompositePredicate<Method> predicate = builder.predicate();
      return new MemberSelector<Method>(finder, predicate, recursively).in(target);
    }
    MethodsFinder finder = new MethodsFinder();
    MembersSelector<Method> selector = new MembersSelector<Method>(finder);
    if(recursively) {
      selector.useHierarchy();
    }
    Set<Method> set = selector.in(target);
    try {
      Predicate<Method> predicate = builder.predicate().and(ReflectionPredicates.named(name));
      return selectFrom(set).element(predicate);
    } catch (SearchException e) {
      throw new ReflectionException(e);
    }
  }

  public MethodSelector recursively() {
    recursively = true;
    return this;
  }

  public MethodSelector withoutParameters() {
    return withParameters();
  }

  public CompositePredicate<Method> toPredicate() {
    CompositePredicate<Method> predicate = builder.predicate();
    if(parameterTypes != null) {
      predicate = predicate.and(ReflectionPredicates.withParameters(parameterTypes));
    }
    return predicate;
  }

  /**
   * @return the predicate builder used by this object.
   */
  protected final PredicateBuilder<Method> builder() {
    return builder;
  }

  /**
   * @return <code>true</code> if recursion must be used.
   */
  protected final boolean useHierarchy() {
    return recursively;
  }

  /**
   * @return the field name for search.
   */
  protected final String name() {
    return name;
  }

  /**
   * @return the specified parameter types.
   */
  protected final Class[] parameterTypes() {
    return parameterTypes;
  }

}
