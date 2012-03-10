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
package net.sf.trugger.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.FieldSetterMethodSelector;

/**
 * Default implementation for the setter method selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFieldSetterMethodSelector implements FieldSetterMethodSelector {

  private final MembersSelector<Method> selector;
  private final Field field;

  public TruggerFieldSetterMethodSelector(Field field) {
    this.field = field;
    this.selector = new MembersSelector<Method>(new SettersFinder(field.getName()));
  }

  public FieldSetterMethodSelector annotated() {
    selector.builder().add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public FieldSetterMethodSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public FieldSetterMethodSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.annotatedWith(type));
    return this;
  }

  public FieldSetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }

  public FieldSetterMethodSelector thatMatches(Predicate<? super Method> predicate) {
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
