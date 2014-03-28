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
import org.atatec.trugger.selector.ClassesSelector;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Marcelo Guimarães
 */
public class TruggerClassesSelector implements ClassesSelector {

  private final Scanner scanner;
  private final boolean deepScan;
  private final Predicate<? super Class> predicate;

  public TruggerClassesSelector(Scanner scanner, boolean deepScan) {
    this.scanner = scanner;
    this.deepScan = deepScan;
    this.predicate = null;
  }

  public TruggerClassesSelector(Scanner scanner, boolean deepScan,
                                Predicate<? super Class> predicate) {
    this.scanner = scanner;
    this.deepScan = deepScan;
    this.predicate = predicate;
  }

  public ClassesSelector filter(Predicate<? super Class> predicate) {
    return new TruggerClassesSelector(scanner, deepScan, predicate);
  }

  public List<Class> in(String packageName) throws ClassScanningException {
    List<Class> classes;
    try {
      if (deepScan) {
        classes = scanner.deepScan(packageName);
      } else {
        classes = scanner.scan(packageName);
      }
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
    if (predicate != null) {
      return classes.stream().filter(predicate).collect(Collectors.toList());
    }
    return classes;
  }

}
