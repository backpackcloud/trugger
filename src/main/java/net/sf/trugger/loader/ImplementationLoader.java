/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.loader;

import net.sf.trugger.loader.impl.TruggerRegistry;
import net.sf.trugger.registry.Registry;

/**
 * This is the main class of this API, it loads implementations for the
 * interfaces.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ImplementationLoader {

  private final Registry<Class<?>, Object> registry;

  public ImplementationLoader(Registry<Class<?>, Object> registry) {
    this.registry = registry;
  }

  /**
   * Returns the implementation for a given class.
   * <p>
   * The implementation will be an instance of the resolved class for the key
   * <code>type</code>.
   *
   * @return the implementation for the given class.
   */
  public final <E> E get(Class<E> type) {
    return (E) registry.registryFor(type);
  }

  /**
   * Returns the register for manual configuration.
   *
   * @return the register used by this instance.
   * @since 2.3
   */
  public final Registry<Class<?>, Object> registry() {
    return registry;
  }

  private static class ImplementationLoaderHolder {
    private static final ImplementationLoader instance = new ImplementationLoader(new TruggerRegistry());
  }

  /**
   * Returns this class shared instance, which is used by the entire framework
   * to load implementations.
   *
   * @return the shared instance.
   */
  public static ImplementationLoader getInstance() {
    return ImplementationLoaderHolder.instance;
  }

}
