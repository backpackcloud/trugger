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
import java.lang.reflect.Method;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.GetterMethodSelector;

/**
 * A default implementation for the getter method selector.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerGetterMethodSelector implements GetterMethodSelector {
  
  private final PredicateBuilder<Method> builder = new PredicateBuilder<Method>();
  private boolean recursively;
  private final String name;
  
  public TruggerGetterMethodSelector(String name) {
    this.name = name;
  }
  
  public GetterMethodSelector annotated() {
    builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public GetterMethodSelector notAnnotated() {
    builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }
  
  public GetterMethodSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.annotatedWith(type));
    return this;
  }
  
  public GetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }
  
  public GetterMethodSelector thatMatches(Predicate<? super Method> predicate) {
    builder.add(predicate);
    return this;
  }
  
  public Method in(Object target) {
    return new MemberSelector<Method>(new GetterFinder(name), builder.predicate(), recursively).in(target);
  }
  
  public GetterMethodSelector recursively() {
    recursively = true;
    return this;
  }
  
  public CompositePredicate<Method> toPredicate() {
    return builder.predicate();
  }
  
}
