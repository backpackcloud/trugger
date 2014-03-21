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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.scan.ScanLevel;
import org.atatec.trugger.selector.ClassSpecifier;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

/**
 * @author Marcelo Guimarães
 */
public class BaseClassSelector implements ClassSpecifier {

  protected Predicate<Class> predicate;
  protected ScanLevel level = ScanLevel.PACKAGE;
  protected final Scanner scanner;

  public BaseClassSelector(Scanner scanner) {
    this.scanner = scanner;
  }

  private void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public ClassSpecifier assignableTo(Class type) {
    add(ReflectionPredicates.assignableTo(type));
    return this;
  }

  public ClassSpecifier recursively() {
    level = ScanLevel.SUBPACKAGES;
    return this;
  }

  public ClassSpecifier that(Predicate<? super Class> predicate) {
    add(predicate);
    return this;
  }

  public ClassSpecifier annotatedWith(Class<? extends Annotation> annotationType) {
    add(ReflectionPredicates.isAnnotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier notAnnotatedWith(Class<? extends Annotation> annotationType) {
    add(ReflectionPredicates.isNotAnnotatedWith(annotationType));
    return this;
  }

  public ClassSpecifier annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ClassSpecifier notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

}
