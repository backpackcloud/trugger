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
package net.sf.trugger.scan;

import net.sf.trugger.registry.Registry;

/**
 * Interface that defines a factory for the class finding operation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public interface ClassScannerFactory {

  /**
   * Returns the registered {@link ResourceFinder} for the specified protocol.
   *
   * @param protocol
   *          the protocol
   * @return the registered resource for the specified protocol.
   * @throws ClassScanningException
   *           if no finder is registered for the given protocol.
   */
  ResourceFinder finderFor(String protocol) throws ClassScanningException;

  /**
   * Returns the registry that associates protocol names to finders.
   *
   * @return the registry.
   */
  Registry<String, ResourceFinder> registry();

  /**
   * Creates the implementation for the class finding operation.
   *
   * @return the implementation for this operation.
   */
  ClassScanner createClassScanner();

}
