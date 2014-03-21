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

import org.atatec.trugger.reflection.FieldPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.FieldSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * A default implementation for the field selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerFieldSelector implements FieldSelector {

  protected Predicate<Field> predicate;
  private String name;
  private boolean recursively;
  protected MemberFindersRegistry registry;

  public TruggerFieldSelector(String name, MemberFindersRegistry registry) {
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

  public TruggerFieldSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public FieldSelector nonStatic() {
    add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }

  public FieldSelector nonFinal() {
    add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
    return this;
  }

  public FieldSelector that(Predicate<? super Field> predicate) {
    add(predicate);
    return this;
  }

  public FieldSelector ofType(Class<?> type) {
    add(FieldPredicates.ofType(type));
    return this;
  }

  public FieldSelector assignableTo(Class<?> type) {
    add(FieldPredicates.assignableTo(type));
    return this;
  }

  public FieldSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public FieldSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public FieldSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public FieldSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public Field in(Object target) {
    return new MemberSelector<>(registry.fieldFinder(name), predicate, recursively).in(target);
  }

  public FieldSelector recursively() {
    recursively = true;
    return this;
  }

  /** @return <code>true</code> if recursion must be used. */
  protected final boolean useHierarchy() {
    return recursively;
  }

  /** @return the field name for search. */
  protected final String name() {
    return name;
  }

}
