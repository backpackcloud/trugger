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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.reflection.ClassPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.scan.ScanLevel;
import org.atatec.trugger.selector.ClassSpecifier;

import java.lang.annotation.Annotation;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class BaseClassSelector implements ClassSpecifier {

  protected PredicateBuilder<Class> builder = new PredicateBuilder<Class>(null);
  protected ScanLevel level = ScanLevel.PACKAGE;
  protected final Scanner scanner;

  public BaseClassSelector(Scanner scanner) {
    this.scanner = scanner;
  }

  public ClassSpecifier assignableTo(Class type) {
    this.builder.add(ReflectionPredicates.assignableTo(type));
    return this;
  }

  @Override
  public ClassSpecifier instantiable() {
    this.builder.add(ClassPredicates.INSTANTIABLE);
    return this;
  }

  public ClassSpecifier recursively() {
    level = ScanLevel.SUBPACKAGES;
    return this;
  }

  public ClassSpecifier that(Predicate<? super Class> predicate) {
    this.builder.add(predicate);
    return this;
  }

  public ClassSpecifier annotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.isAnnotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier notAnnotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.isNotAnnotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier annotated() {
    this.builder.add(ReflectionPredicates.IS_ANNOTATED);
    return this;
  }

  public ClassSpecifier notAnnotated() {
    this.builder.add(ReflectionPredicates.IS_NOT_ANNOTATED);
    return this;
  }

  public CompositePredicate<Class> toPredicate() {
    return builder.predicate();
  }

}
