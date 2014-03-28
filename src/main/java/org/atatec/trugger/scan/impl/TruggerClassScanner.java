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

import org.atatec.trugger.scan.*;
import org.atatec.trugger.selector.ClassesSelector;

/**
 * A default class finder.
 *
 * @author Marcelo Guimarães
 */
public class TruggerClassScanner implements ClassScanner {

  private final ClassScannerFactory factory;
  private final ClassLoader classLoader;

  /**
   * Creates a new finder using the context class loader in the current Thread.
   */
  public TruggerClassScanner(ClassScannerFactory factory) {
    this(Thread.currentThread().getContextClassLoader(), factory);
  }

  public TruggerClassScanner(ClassLoader classLoader,
                             ClassScannerFactory factory) {
    this.classLoader = classLoader;
    this.factory = factory;
  }

  @Override
  public ClassScanner with(ClassLoader classLoader) {
    return new TruggerClassScanner(classLoader, factory);
  }

  public ClassesSelector classes() {
    return new TruggerClassesSelector(new TruggerScanner(factory, classLoader));
  }

}
