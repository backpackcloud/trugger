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
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.selector.ClassesSelector;
import net.sf.trugger.util.Utils;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerClassesSelector extends AbstractClassSelector implements ClassesSelector {

  public TruggerClassesSelector(Scanner scanner) {
    super(scanner);
  }

  public ClassesSelector annotated() {
    super.annotated();
    return this;
  }

  public ClassesSelector annotatedWith(Class<? extends Annotation> annotationType) {
    super.annotatedWith(annotationType);
    return this;
  }

  public ClassesSelector anonymous() {
    super.anonymous();
    return this;
  }

  public ClassesSelector assignableTo(Class<?> type) {
    super.assignableTo(type);
    return this;
  }

  public ClassesSelector nonAnonymous() {
    super.nonAnonymous();
    return this;
  }

  public ClassesSelector notAnnotated() {
    super.notAnnotated();
    return this;
  }

  public ClassesSelector notAnnotatedWith(Class<? extends Annotation> annotationType) {
    super.notAnnotatedWith(annotationType);
    return this;
  }

  public ClassesSelector recursively() {
    super.recursively();
    return this;
  }

  public ClassesSelector thatMatches(Predicate<? super Class<?>> predicate) {
    super.thatMatches(predicate);
    return this;
  }

  public ClassesSelector withAccess(Access access) {
    super.withAccess(access);
    return this;
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

  public Set<Class<?>> fromHere() throws ClassScanningException {
    return in(Utils.classAtStackTrace(4).getPackage().getName());
  }

}
