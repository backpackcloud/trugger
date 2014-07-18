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
package tools.devnull.trugger.scan;

import tools.devnull.trugger.util.ImplementationLoader;

/**
 * A helper class for finding classes.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public class ClassScan {

  private static final ClassScannerFactory factory;

  private ClassScan() {
  }

  static {
    factory = ImplementationLoader.get(ClassScannerFactory.class);
  }

  /**
   * @return a new {@link ClassScanner}.
   */
  public static ClassScanner scan() {
    return factory.createClassScanner();
  }

  /**
   * @see ClassScannerFactory#register(ResourceFinder...)
   * @since 4.0
   */
  public static void register(ResourceFinder... finders) {
    factory.register(finders);
  }

}
