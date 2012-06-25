/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.registry.Registry;
import org.atatec.trugger.selector.ClassSelector;
import org.atatec.trugger.selector.ClassesSelector;

/**
 * A helper class for finding classes.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class ClassScan {

  private static final ClassScannerFactory factory;

  private ClassScan() {}

  static {
    factory = ImplementationLoader.getInstance().get(ClassScannerFactory.class);
  }

  /**
   * @return a new {@link ClassScanner}.
   */
  public static ClassScanner newScan() {
    return factory.createClassScanner();
  }

  /**
   * @return the registry.
   */
  public static Registry<String, ResourceFinder> registry() {
    return factory.registry();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findClasses();
   * </pre>
   */
  public static ClassesSelector findClasses() {
    return newScan().findClasses();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findClass();
   * </pre>
   *
   * @since 2.5
   */
  public static ClassSelector findClass() {
    return newScan().findClass();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findInterfaces();
   * </pre>
   */
  public static ClassesSelector findInterfaces() {
    return newScan().findInterfaces();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findEnums();
   * </pre>
   */
  public static ClassesSelector findEnums() {
    return newScan().findEnums();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findAnnotations();
   * </pre>
   */
  public static ClassesSelector findAnnotations() {
    return newScan().findAnnotations();
  }

  /**
   * This method has the same effect as:
   *
   * <pre>
   * newScan().findAll();
   * </pre>
   */
  public static ClassesSelector findAll() {
    return newScan().findAll();
  }

}
