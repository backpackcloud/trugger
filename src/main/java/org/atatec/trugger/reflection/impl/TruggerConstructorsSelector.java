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
import org.atatec.trugger.selector.ConstructorsSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * A default implementation for the constructors selector.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerConstructorsSelector implements ConstructorsSelector {

  private final MembersSelector<Constructor<?>> selector;

  public TruggerConstructorsSelector(MembersFinder<Constructor<?>> finder) {
    selector = new MembersSelector<Constructor<?>>(finder);
  }

  public ConstructorsSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public ConstructorsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public ConstructorsSelector annotated() {
    selector.builder().add(ReflectionPredicates.IS_ANNOTATED);
    return this;
  }

  public ConstructorsSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.IS_NOT_ANNOTATED);
    return this;
  }

  public ConstructorsSelector that(Predicate<? super Constructor<?>> predicate) {
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
