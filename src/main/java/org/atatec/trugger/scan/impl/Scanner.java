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

import org.atatec.trugger.scan.PackageScan;

import java.io.IOException;
import java.util.Set;

/**
 * Interface that defines a scanner for a package entry.
 *
 * @author Marcelo Guimarães
 */
public interface Scanner {

  /**
   * Sets the class loader to use.
   *
   * @param classLoader
   *          the class loader to use.
   */
  void setClassLoader(ClassLoader classLoader);

  /**
   * Scans and returns the found classes in the specified package.
   *
   * @param packageEntry
   *          the package to scan.
   * @return the classes found in the package
   */
  Set<Class> scanPackage(PackageScan packageEntry) throws IOException, ClassNotFoundException;

}
