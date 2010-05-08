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
package net.sf.trugger.scan.impl;

import java.lang.annotation.Annotation;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.scan.ScanLevel;
import net.sf.trugger.selector.ClassSpecifier;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AbstractClassSelector implements ClassSpecifier {

  protected PredicateBuilder<Class<?>> builder = new PredicateBuilder<Class<?>>(null);
  protected ScanLevel level = ScanLevel.PACKAGE;
  protected final Scanner scanner;

  public AbstractClassSelector(Scanner scanner) {
    this.scanner = scanner;
  }

  public ClassSpecifier assignableTo(Class<?> type) {
    this.builder.add(ReflectionPredicates.assignableTo(type));
    return this;
  }

  public ClassSpecifier recursively() {
    level = ScanLevel.SUBPACKAGES;
    return this;
  }

  public ClassSpecifier thatMatches(Predicate<? super Class<?>> predicate) {
    this.builder.add(predicate);
    return this;
  }

  public ClassSpecifier annotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.annotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier notAnnotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.notAnnotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier annotated() {
    this.builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ClassSpecifier notAnnotated() {
    this.builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public ClassSpecifier anonymous() {
    this.builder.add(ReflectionPredicates.ANONYMOUS);
    return this;
  }

  public ClassSpecifier nonAnonymous() {
    this.builder.add(ReflectionPredicates.NON_ANONYMOUS);
    return this;
  }

  public ClassSpecifier withAccess(Access access) {
    this.builder.add(access.classPredicate());
    return this;
  }

  public CompositePredicate<Class<?>> toPredicate() {
    CompositePredicate<Class<?>> predicate = builder.predicate();
    return predicate == null ? Predicates.ALWAYS_TRUE : predicate;
  }

}