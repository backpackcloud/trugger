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
import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.selector.ClassSelector;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerClassSelector extends AbstractClassSelector implements ClassSelector {

  public TruggerClassSelector(Scanner scanner) {
    super(scanner);
    builder = new PredicateBuilder<Class<?>>();
  }

  public ClassSelector annotated() {
    super.annotated();
    return this;
  }

  public ClassSelector annotatedWith(Class<? extends Annotation> annotationType) {
    super.annotatedWith(annotationType);
    return this;
  }

  public ClassSelector anonymous() {
    super.anonymous();
    return this;
  }

  public ClassSelector assignableTo(Class<?> type) {
    super.assignableTo(type);
    return this;
  }

  public ClassSelector nonAnonymous() {
    super.nonAnonymous();
    return this;
  }

  public ClassSelector notAnnotated() {
    super.notAnnotated();
    return this;
  }

  public ClassSelector notAnnotatedWith(Class<? extends Annotation> annotationType) {
    super.notAnnotatedWith(annotationType);
    return this;
  }

  public ClassSelector recursively() {
    super.recursively();
    return this;
  }

  public ClassSelector thatMatches(Predicate<? super Class<?>> predicate) {
    super.thatMatches(predicate);
    return this;
  }

  public ClassSelector withAccess(Access access) {
    super.withAccess(access);
    return this;
  }

  public Class<?> in(String... packageNames) throws ClassScanningException {
    return in(level.createScanPackages(packageNames));
  }

  public Class<?> in(PackageScan packageToScan) throws ClassScanningException {
    return in(Arrays.asList(packageToScan));
  }

  public Class<?> in(Collection<PackageScan> packagesToScan) throws ClassScanningException {
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
    try {
      return Iteration.selectFrom(classes).elementMatching(builder.predicate());
    } catch (SearchException e) {
      throw new ClassScanningException(e);
    }
  }

}
