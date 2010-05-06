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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.scan.ScanLevel;
import net.sf.trugger.selector.ClassSelector;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerClassSelector implements ClassSelector {

  private PredicateBuilder<Class<?>> builder = new PredicateBuilder<Class<?>>(null);
  private ScanLevel level = ScanLevel.PACKAGE;

  private final Scanner scanner;

  public TruggerClassSelector(Scanner scanner) {
    this.scanner = scanner;
  }

  public ClassSelector assignableTo(Class<?> type) {
    this.builder.add(ReflectionPredicates.assignableTo(type));
    return this;
  }

  public ClassSelector recursively() {
    level = ScanLevel.SUBPACKAGES;
    return this;
  }

  public ClassSelector thatMatches(Predicate<? super Class<?>> predicate) {
    this.builder.add(predicate);
    return this;
  }

  public ClassSelector annotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.annotatedWith(annotationType));
    return this;
  }

  public ClassSelector notAnnotatedWith(Class<? extends Annotation> annotationType) {
    this.builder.add(ReflectionPredicates.notAnnotatedWith(annotationType));
    return this;
  }

  public ClassSelector annotated() {
    this.builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ClassSelector notAnnotated() {
    this.builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public ClassSelector anonymous() {
    this.builder.add(ReflectionPredicates.ANONYMOUS);
    return this;
  }

  public ClassSelector nonAnonymous() {
    this.builder.add(ReflectionPredicates.NON_ANONYMOUS);
    return this;
  }

  public ClassSelector withAccess(Access access) {
    this.builder.add(access.classPredicate());
    return this;
  }

  public CompositePredicate<Class<?>> toPredicate() {
    CompositePredicate<Class<?>> predicate = builder.predicate();
    return predicate == null ? Predicates.ALWAYS_TRUE : predicate;
  }

  public Set<Class<?>> in(String... packageNames) throws ClassScanningException {
    return in(level.createScanPackages(packageNames));
  }

  public Set<Class<?>> in(PackageScan packageToScan) throws ClassScanningException {
    return in(Arrays.asList(packageToScan));
  }

  public Set<Class<?>> in(Collection<PackageScan> packagesToScan) throws ClassScanningException {
    Set<Class<?>> classes = new HashSet<Class<?>>(40);
    try {
      for (PackageScan entry : packagesToScan) {
        classes.addAll(scanner.scanPackage(entry));
      }
    } catch (IOException e) {
      throw new ClassScanningException(e);
    } catch (ClassNotFoundException e) {
      throw new ClassScanningException(e);
    }
    Predicate<Class<?>> predicate = builder.predicate();
    if (predicate != null) {
      Iteration.retainFrom(classes).elementsMatching(predicate);
    }
    return classes;
  }

}
