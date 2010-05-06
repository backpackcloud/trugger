/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple registry that uses a {@link Map} as the storage.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class MapRegistry<K, V> implements Registry<K, V> {

  /**
   * The map used by this register.
   */
  protected final Map<K, V> map;

  public MapRegistry() {
    this(new HashMap<K, V>());
  }

  public MapRegistry(Map<K, V> map) {
    this.map = map;
  }

  public V registryFor(K key) {
    return map.get(key);
  }

  public boolean hasRegistryFor(K key) {
    return map.containsKey(key);
  }

  public V removeRegistryFor(K key) {
    return map.remove(key);
  }

  public RegistryMapper<K, V> register(final V value) {
    return new RegistryMapper<K, V>() {
      public Registry<K, V> to(K key) {
        map.put(key, value);
        return MapRegistry.this;
      };
    };
  }

  public Set<Entry<K, V>> entries() {
    Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
    for (java.util.Map.Entry<K, V> entry : map.entrySet()) {
      set.add(new RegistryEntry<K, V>(entry.getKey(), entry.getValue()));
    }
    return set;
  }

}
