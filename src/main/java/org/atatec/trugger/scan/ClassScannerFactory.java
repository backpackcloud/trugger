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
package org.atatec.trugger.scan;

/**
 * Interface that defines a factory for the class finding operation.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public interface ClassScannerFactory {

  /**
   * Returns the registered {@link ResourceFinder} for the specified protocol.
   *
   * @param protocol the protocol
   *
   * @return the registered resource for the specified protocol.
   *
   * @throws NoResourceFinderException if no finder is registered for the given protocol.
   */
  ResourceFinder finderFor(String protocol) throws NoResourceFinderException;

  /**
   * Registers the given resource finders. Previous registered ResourceFinder will be
   * replaced.
   *
   * @param finders the resource finder to register
   *
   * @since 4.0
   */
  void register(ResourceFinder... finders);

  /**
   * Creates the implementation for the class finding operation.
   *
   * @return the implementation for this operation.
   */
  ClassScanner createClassScanner();

}
