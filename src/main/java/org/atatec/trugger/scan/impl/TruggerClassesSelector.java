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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.scan.ClassScanningException;
import org.atatec.trugger.scan.PackageScan;
import org.atatec.trugger.scan.ScanLevel;
import org.atatec.trugger.selector.ClassesSelector;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Marcelo Guimarães
 */
public class TruggerClassesSelector implements ClassesSelector {

  private final Scanner scanner;
  private final ScanLevel level;
  private final Predicate<? super Class> predicate;

  public TruggerClassesSelector(Scanner scanner) {
    this.scanner = scanner;
    this.level = ScanLevel.PACKAGE;
    this.predicate = null;
  }

  public TruggerClassesSelector(Scanner scanner, ScanLevel level,
                                Predicate<? super Class> predicate) {
    this.scanner = scanner;
    this.level = level;
    this.predicate = predicate;
  }

  public ClassesSelector filter(Predicate<? super Class> predicate) {
    return new TruggerClassesSelector(scanner, level, predicate);
  }

  public ClassesSelector recursively() {
    return new TruggerClassesSelector(scanner, ScanLevel.SUBPACKAGES, predicate);
  }

  public Set<Class> in(String... packageNames) throws ClassScanningException {
    return in(level.createScanPackages(packageNames));
  }

  public Set<Class> in(PackageScan packageToScan) throws ClassScanningException {
    return in(Arrays.asList(packageToScan));
  }

  public Set<Class> in(Collection<PackageScan> packagesToScan) throws ClassScanningException {
    Set<Class> classes = new HashSet<>(40);
    try {
      for (PackageScan entry : packagesToScan) {
        classes.addAll(scanner.scanPackage(entry));
      }
    } catch (IOException e) {
      throw new ClassScanningException(e);
    } catch (ClassNotFoundException e) {
      throw new ClassScanningException(e);
    }
    if (predicate != null) {
      return classes.stream().filter(predicate).collect(Collectors.toSet());
    }
    return classes;
  }

}
