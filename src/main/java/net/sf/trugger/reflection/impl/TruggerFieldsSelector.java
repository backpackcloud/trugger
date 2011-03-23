/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.FieldsSelector;

/**
 * A default implementation for the fields selector.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFieldsSelector implements FieldsSelector {
  
  private MembersSelector<Field> selector = new MembersSelector<Field>(new FieldsFinder());
  
  public FieldsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.annotatedWith(type));
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
    selector.builder().add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }
  
  public FieldsSelector nonStatic() {
    selector.builder().add(ReflectionPredicates.NON_STATIC);
    return this;
  }
  
  public FieldsSelector nonFinal() {
    selector.builder().add(ReflectionPredicates.NON_FINAL);
    return this;
  }
  
  public FieldsSelector withAccess(Access access) {
    selector.builder().add(access.memberPredicate());
    return this;
  }
  
  public FieldsSelector thatMatches(Predicate<? super Field> predicate) {
    selector.builder().add(predicate);
    return this;
  }
  
  public FieldsSelector ofType(final Class<?> type) {
    selector.builder().add(ReflectionPredicates.ofType(type, false));
    return this;
  }
  
  public FieldsSelector assignableTo(Class<?> type) {
    selector.builder().add(ReflectionPredicates.ofType(type, true));
    return this;
  }
  
  public Set<Field> in(Object target) {
    return selector.in(target);
  }
  
  public FieldsSelector recursively() {
    selector.useHierarchy();
    return this;
  }
  
  public CompositePredicate<Field> toPredicate() {
    return selector.toPredicate();
  }
  
}
