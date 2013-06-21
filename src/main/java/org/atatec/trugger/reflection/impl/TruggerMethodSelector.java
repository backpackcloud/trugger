/*
 * Copyright 2009-2012 Marcelo Guimarães
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

import org.atatec.trugger.iteration.NonUniqueMatchException;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.reflection.MethodPredicates;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.MethodSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * A default implementation for the method selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerMethodSelector implements MethodSelector {

  private final PredicateBuilder<Method> builder = new PredicateBuilder<Method>();
  private String name;
  private boolean recursively;
  private Class[] parameterTypes;
  protected MemberFindersRegistry registry;

  public TruggerMethodSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
  }

  public TruggerMethodSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public MethodSelector nonStatic() {
    builder.add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }

  public MethodSelector nonFinal() {
    builder.add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
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
    builder.add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public MethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public MethodSelector returning(Class<?> returnType) {
    builder.add(MethodPredicates.returns(returnType));
    return this;
  }

  public MethodSelector withoutReturnType() {
    builder.add(MethodPredicates.returns(Void.TYPE));
    return this;
  }

  public MethodSelector withParameters(final Class<?>... parameterTypes) {
    this.parameterTypes = parameterTypes;
    return this;
  }

  public Method in(Object target) {
    if (parameterTypes != null) {
      final CompositePredicate<Method> predicate = builder.predicate();
      return new MemberSelector<Method>(registry.methodFinder(name, parameterTypes),
        predicate, recursively).in(target);
    }
    MembersSelector<Method> selector = new MembersSelector<Method>(registry.methodsFinder());
    if (recursively) {
      selector.useHierarchy();
    }
    Set<Method> methods = selector.in(target);
    try {
      return builder.add(ReflectionPredicates.named(name)).findIn(methods);
    } catch (NonUniqueMatchException e) {
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

  /** @return the predicate builder used by this object. */
  protected final PredicateBuilder<Method> builder() {
    return builder;
  }

  /** @return <code>true</code> if recursion must be used. */
  protected final boolean useHierarchy() {
    return recursively;
  }

  /** @return the field name for search. */
  protected final String name() {
    return name;
  }

  /** @return the specified parameter types. */
  protected final Class[] parameterTypes() {
    return parameterTypes;
  }

}
