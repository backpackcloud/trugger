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
import java.lang.reflect.Constructor;
import java.util.Set;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.ConstructorsSelector;

/**
 * A default implementation for the constructors selector.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerConstructorsSelector implements ConstructorsSelector {
  
  private final MembersSelector<Constructor<?>> selector =
    new MembersSelector<Constructor<?>>(new ConstructorsFinder());
  
  public ConstructorsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.annotatedWith(type));
    return this;
  }
  
  public ConstructorsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }
  
  public ConstructorsSelector annotated() {
    selector.builder().add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public ConstructorsSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }
  
  public ConstructorsSelector withAccess(Access access) {
    selector.builder().add(access.memberPredicate());
    return this;
  }
  
  public ConstructorsSelector thatMatches(Predicate<? super Constructor<?>> predicate) {
    selector.builder().add(predicate);
    return this;
  }
  
  public Set<Constructor<?>> in(Object target) {
    return selector.in(target);
  }
  
  public CompositePredicate<Constructor<?>> toPredicate() {
    return selector.toPredicate();
  }
  
}
