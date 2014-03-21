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
import org.atatec.trugger.selector.FieldGetterMethodSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * A default implementation for the getter method selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerFieldGetterMethodSelector implements FieldGetterMethodSelector {

  private Predicate<Method> predicate;
  private final Field field;

  private MembersFinder<Method> finder;

  public TruggerFieldGetterMethodSelector(Field field, MembersFinder<Method> finder) {
    this.field = field;
    this.finder = finder;
    add(MethodPredicates.returns(field.getType()));
  }

  public void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public FieldGetterMethodSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public FieldGetterMethodSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public FieldGetterMethodSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public FieldGetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public FieldGetterMethodSelector that(Predicate<? super Method> predicate) {
    add(predicate);
    return this;
  }

  public Method in(Object target) {
    return new MemberSelector<>(
        new GetterFinder(field.getName(), finder), predicate)
        .in(target);
  }

}
