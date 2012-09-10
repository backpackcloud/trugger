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

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.FieldSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * A default implementation for the field selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFieldSelector implements FieldSelector {

  private final PredicateBuilder<Field> builder = new PredicateBuilder<Field>();
  private String name;
  private boolean recursively;
  protected MemberFindersRegistry registry;

  public TruggerFieldSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
  }

  public TruggerFieldSelector(MemberFindersRegistry registry) {
    this.registry = registry;
  }

  public FieldSelector nonStatic() {
    builder.add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }

  public FieldSelector nonFinal() {
    builder.add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
    return this;
  }

  public FieldSelector that(Predicate<? super Field> predicate) {
    builder.add(predicate);
    return this;
  }

  public FieldSelector ofType(Class<?> type) {
    builder.add(ReflectionPredicates.ofType(type, false));
    return this;
  }

  public FieldSelector assignableTo(Class<?> type) {
    builder.add(ReflectionPredicates.ofType(type, true));
    return this;
  }

  public FieldSelector annotated() {
    builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public FieldSelector notAnnotated() {
    builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public FieldSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.annotatedWith(type));
    return this;
  }

  public FieldSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }

  public Field in(Object target) {
    return new MemberSelector<Field>(registry.fieldFinder(name), builder.predicate(), recursively).in(target);
  }

  public FieldSelector recursively() {
    recursively = true;
    return this;
  }

  public CompositePredicate<Field> toPredicate() {
    return builder.predicate();
  }

  /** @return the predicate builder used by this object. */
  protected final PredicateBuilder<Field> builder() {
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

}
