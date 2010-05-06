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
package net.sf.trugger.selector;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

import net.sf.trugger.Result;
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.scan.ScanLevel;

/**
 * Interface that defines a class capable of configure a scan for classes.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public interface ClassSelector extends AccessSelector, PredicateSelector<Class<?>>, AnnotatedElementSelector,
    RecursionSelector, Predicable<Class<?>>, Result<Set<Class<?>>, PackageScan> {

  ClassSelector withAccess(Access access);

  ClassSelector thatMatches(Predicate<? super Class<?>> predicate);

  ClassSelector annotatedWith(Class<? extends Annotation> type);

  ClassSelector notAnnotatedWith(Class<? extends Annotation> type);

  ClassSelector annotated();

  ClassSelector notAnnotated();

  /**
   * Selects only the anonymous classes.
   *
   * @return a reference to this instance.
   */
  ClassSelector anonymous();

  /**
   * Selects only the non anonymous classes.
   *
   * @return a reference to this instance.
   */
  ClassSelector nonAnonymous();

  /**
   * Searchs recursively in the packages (sets the scan level to
   * {@link ScanLevel#SUBPACKAGES}). Is a common way to scan packages and its
   * subpackages but only take effect if using with {@link #in(String...)} .
   */
  ClassSelector recursively();

  /**
   * Selects the classes that are assignable to the specified class.
   *
   * @param type
   *          the class that must be assignable from the found classes.
   * @return a reference to this object.
   */
  ClassSelector assignableTo(Class<?> type);

  /**
   * Scans for the classes in the specified packages.
   *
   * @param packagesToScan
   *          the collection of the packages and their scan levels.
   * @return the found classes.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  Set<Class<?>> in(Collection<PackageScan> packagesToScan) throws ClassScanningException;

  /**
   * Scans for the classes in the specified package.
   *
   * @param packageToScan
   *          the package to scan.
   * @return the found classes.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  Set<Class<?>> in(PackageScan packageToScan) throws ClassScanningException;

  /**
   * Selects the classes in the specified packages using the scan level
   * {@link ScanLevel#PACKAGE}. If the scan is {@link #recursively()
   * recursively}, then the scan level will be {@link ScanLevel#SUBPACKAGES}.
   *
   * @param packageNames
   *          the packages to scan.
   * @return the found classes.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  Set<Class<?>> in(String... packageNames) throws ClassScanningException;

}
