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
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.FieldSetterMethodSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Default implementation for the setter method selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFieldSetterMethodSelector implements FieldSetterMethodSelector {

  private final MembersSelector<Method> selector;
  private final Field field;

  public TruggerFieldSetterMethodSelector(Field field, MembersFinder<Method> finder) {
    this.field = field;
    this.selector = new MembersSelector<Method>(new SettersFinder(field.getName(), finder));
  }

  public FieldSetterMethodSelector annotated() {
    selector.builder().add(ReflectionPredicates.IS_ANNOTATED);
    return this;
  }

  public FieldSetterMethodSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.IS_NOT_ANNOTATED);
    return this;
  }

  public FieldSetterMethodSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public FieldSetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public FieldSetterMethodSelector that(Predicate<? super Method> predicate) {
    selector.builder().add(predicate);
    return this;
  }

  public Method in(Object target) {
    return new SetterResult(selector, field.getType()).in(target);
  }

  public CompositePredicate<Method> toPredicate() {
    return selector.toPredicate();
  }

}
