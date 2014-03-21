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

import org.atatec.trugger.reflection.MethodPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.MethodsSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A default implementation for the methods selector.
 * 
 * @author Marcelo Guimarães
 */
public class TruggerMethodsSelector implements MethodsSelector {
  
  private MembersSelector<Method> selector;

  public TruggerMethodsSelector(MembersFinder<Method> finder) {
    selector = new MembersSelector<>(finder);
  }

  public MethodsSelector annotated() {
    selector.add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public MethodsSelector notAnnotated() {
    selector.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }
  
  public MethodsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }
  
  public MethodsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }
  
  public MethodsSelector nonStatic() {
    selector.add(ReflectionPredicates.dontDeclare(Modifier.STATIC));
    return this;
  }
  
  public MethodsSelector nonFinal() {
    selector.add(ReflectionPredicates.dontDeclare(Modifier.FINAL));
    return this;
  }
  
  public MethodsSelector that(Predicate<? super Method> predicate) {
    selector.add(predicate);
    return this;
  }
  
  public MethodsSelector named(String name) {
    selector.add(ReflectionPredicates.named(name));
    return this;
  }
  
  public MethodsSelector withParameters(Class<?>... parameterTypes) {
    selector.add(MethodPredicates.takes(parameterTypes));
    return this;
  }
  
  public MethodsSelector returning(Class<?> returnType) {
    selector.add(MethodPredicates.returns(returnType));
    return this;
  }
  
  public MethodsSelector withoutReturnType() {
    selector.add(MethodPredicates.returns(Void.TYPE));
    return this;
  }
  
  public MethodsSelector withoutParameters() {
    return withParameters();
  }
  
  public Set<Method> in(Object target) {
    return selector.in(target);
  }
  
  public MethodsSelector recursively() {
    selector.useHierarchy();
    return this;
  }
  
}
