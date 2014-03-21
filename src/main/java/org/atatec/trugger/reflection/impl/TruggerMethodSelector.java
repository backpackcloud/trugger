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

import org.atatec.trugger.reflection.MethodPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.MethodSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * A default implementation for the method selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerMethodSelector implements MethodSelector {

  protected Predicate<Method> predicate;
  private String name;
  private boolean recursively;
  private Class[] parameterTypes;
  protected MemberFindersRegistry registry;

  public TruggerMethodSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
  }

  protected void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public TruggerMethodSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public MethodSelector nonStatic() {
    add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }

  public MethodSelector nonFinal() {
    add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
    return this;
  }

  public MethodSelector that(Predicate<? super Method> predicate) {
    add(predicate);
    return this;
  }

  public MethodSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public MethodSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public MethodSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public MethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public MethodSelector returning(Class<?> returnType) {
    add(MethodPredicates.returns(returnType));
    return this;
  }

  public MethodSelector withoutReturnType() {
    add(MethodPredicates.returns(Void.TYPE));
    return this;
  }

  public MethodSelector withParameters(final Class<?>... parameterTypes) {
    this.parameterTypes = parameterTypes;
    return this;
  }

  public Method in(Object target) {
    if (parameterTypes != null) {
      return new MemberSelector<>(registry.methodFinder(name, parameterTypes),
          predicate, recursively).in(target);
    }
    MembersSelector<Method> selector = new MembersSelector<Method>(registry.methodsFinder());
    if (recursively) {
      selector.useHierarchy();
    }
    add(ReflectionPredicates.named(name));
    return selector.in(target).stream()
        .filter(predicate)
        .findAny().orElse(null);
  }

  public MethodSelector recursively() {
    recursively = true;
    return this;
  }

  public MethodSelector withoutParameters() {
    return withParameters();
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
