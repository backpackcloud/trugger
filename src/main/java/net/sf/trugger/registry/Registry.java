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
package net.sf.trugger.registry;

import java.util.Set;

/**
 * Interface that defines a registry.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <K>
 *          The key type.
 * @param <V>
 *          The registry type.
 * @since 2.3
 */
public interface Registry<K, V> {

  /**
   * Register the given object.
   *
   * @param value
   *          the object to register.
   * @return a mapper for configuration.
   */
  RegistryMapper<K, V> register(V value);

  /**
   * Checks if the key is registered.
   *
   * @param key
   *          the key to verify.
   * @return <code>true</code> if any registry is mapped to the given key.
   */
  boolean hasRegistryFor(K key);

  /**
   * Returns the registry mapped to the given key. Returns <code>null</code> if
   * no registry is found.
   *
   * @return the registry mapped to the given key.
   */
  V registryFor(K key);

  /**
   * Removes the registry for the given key.
   *
   * @return the removed registry.
   * @since 2.3.1
   */
  V removeRegistryFor(K key);

  /**
   * @return the entries in this registry.
   */
  Set<Entry<K, V>> entries();

  /**
   * Interface that represents a registry entry.
   *
   * @author Marcelo Varella Barca Guimarães
   * @param <K>
   *          The key type.
   * @param <V>
   *          The registry type.
   */
  interface Entry<K, V> {

    /**
     * @return the key.
     */
    K key();

    /**
     * @return the registry.
     */
    V registry();

  }

  /**
   * Interface for mapping a registry to a key.
   *
   * @author Marcelo Varella Barca Guimarães
   * @param <K>
   *          The key type.
   * @param <V>
   *          The registry type.
   */
  interface RegistryMapper<K, V> {

    /**
     * Associates the previously given registry to the given key.
     *
     * @param key
     *          the key to associate.
     * @return a reference to the register.
     */
    Registry<K, V> to(K key);

  }

}
