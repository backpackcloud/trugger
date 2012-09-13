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

import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.reflection.FieldPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.FieldsSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * A default implementation for the fields selector.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFieldsSelector implements FieldsSelector {
  
  private MembersSelector<Field> selector;

  public TruggerFieldsSelector(MembersFinder<Field> finder) {
    selector = new MembersSelector<Field>(finder);
  }

  public FieldsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }
  
  public FieldsSelector annotated() {
    selector.builder().add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public FieldsSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }
  
  public FieldsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }
  
  public FieldsSelector nonStatic() {
    selector.builder().add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }
  
  public FieldsSelector nonFinal() {
    selector.builder().add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
    return this;
  }
  
  public FieldsSelector that(Predicate<? super Field> predicate) {
    selector.builder().add(predicate);
    return this;
  }
  
  public FieldsSelector ofType(final Class<?> type) {
    selector.builder().add(FieldPredicates.ofType(type));
    return this;
  }
  
  public FieldsSelector assignableTo(Class<?> type) {
    selector.builder().add(FieldPredicates.assignableTo(type));
    return this;
  }
  
  public Set<Field> in(Object target) {
    return selector.in(target);
  }
  
  public FieldsSelector recursively() {
    selector.useHierarchy();
    return this;
  }
  
}
