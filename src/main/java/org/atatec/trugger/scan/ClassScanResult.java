/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.scan;

import java.util.Collection;

import org.atatec.trugger.Result;

/**
 * Base interface for scan results.
 *
 * @author Marcelo Guimarães
 * @param <E>
 *          The result type.
 * @since 2.5
 */
public interface ClassScanResult<E> extends Result<E, PackageScan> {

  /**
   * Scans for the classes in the specified packages.
   *
   * @param packagesToScan
   *          the collection of the packages and their scan levels.
   * @return the search result.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  E in(Collection<PackageScan> packagesToScan) throws ClassScanningException;

  /**
   * Scans for the classes in the specified package.
   *
   * @param packageToScan
   *          the package to scan.
   * @return the search result.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  E in(PackageScan packageToScan) throws ClassScanningException;

  /**
   * Uses the scan level {@link ScanLevel#PACKAGE} for doing the search. If the
   * scan is recursively, then the scan level will be {@link ScanLevel#SUBPACKAGES}.
   *
   * @param packageNames
   *          the packages to scan.
   * @return the search result.
   * @throws ClassScanningException
   *           if an error occurs while scanning the packages.
   */
  E in(String... packageNames) throws ClassScanningException;

}
