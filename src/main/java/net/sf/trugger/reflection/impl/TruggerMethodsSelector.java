/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
import java.lang.reflect.Method;
import java.util.Set;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.MethodsSelector;

/**
 * A default implementation for the methods selector.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerMethodsSelector implements MethodsSelector {
  
  private MembersSelector<Method> selector = new MembersSelector<Method>(new MethodsFinder());
  
  public MethodsSelector annotated() {
    selector.builder().add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public MethodsSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }
  
  public MethodsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.annotatedWith(type));
    return this;
  }
  
  public MethodsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }
  
  public MethodsSelector nonStatic() {
    selector.builder().add(ReflectionPredicates.NON_STATIC);
    return this;
  }
  
  public MethodsSelector nonFinal() {
    selector.builder().add(ReflectionPredicates.NON_FINAL);
    return this;
  }
  
  public MethodsSelector withAccess(Access access) {
    selector.builder().add(access.memberPredicate());
    return this;
  }
  
  public MethodsSelector thatMatches(Predicate<? super Method> predicate) {
    selector.builder().add(predicate);
    return this;
  }
  
  public MethodsSelector named(String name) {
    selector.builder().add(ReflectionPredicates.named(name));
    return this;
  }
  
  public MethodsSelector withParameters(Class<?>... parameterTypes) {
    selector.builder().add(ReflectionPredicates.withParameters(parameterTypes));
    return this;
  }
  
  public MethodsSelector returning(Class<?> returnType) {
    selector.builder().add(ReflectionPredicates.ofReturnType(returnType));
    return this;
  }
  
  public MethodsSelector withoutReturnType() {
    selector.builder().add(ReflectionPredicates.ofReturnType(Void.TYPE));
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
  
  public CompositePredicate<Method> toPredicate() {
    return selector.toPredicate();
  }
  
}
