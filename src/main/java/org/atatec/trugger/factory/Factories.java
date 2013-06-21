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
package org.atatec.trugger.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.atatec.trugger.CreateException;

/**
 * An utility class to create {@link Factory} objects.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public final class Factories {

  private Factories() {}

  /**
   * Creates a new Factory that caches the created objects for furter return.
   *
   * @param <K>
   *          The key type.
   * @param <E>
   *          The value type.
   * @param factory
   *          the factory to encapsulate.
   * @return the created factory.
   */
  public static <K, E> Factory<K, E> sharedObjectFactory(Factory<K, E> factory) {
    return new SharedObjectFactory<K, E>(factory);
  }

  /**
   * Creates a new Factory that caches the created objects for furter return.
   *
   * @param <K>
   *          The key type.
   * @param <E>
   *          The value type.
   * @param factory
   *          the factory to encapsulate.
   * @param initialSize
   *          the initial size of the cache.
   * @return the created factory.
   */
  public static <K, E> Factory<K, E> sharedObjectFactory(Factory<K, E> factory, int initialSize) {
    return new SharedObjectFactory<K, E>(factory, initialSize);
  }

  /**
   * Creates a new factory that synchronizes its methods.
   *
   * @param <K>
   *          The key type.
   * @param <E>
   *          The value type.
   * @param factory
   *          the factory to encapsulate
   * @return the created factory.
   */
  public static <K, E> Factory<K, E> synchronizedFactory(final Factory<K, E> factory) {
    return new Factory<K, E>() {

      public synchronized boolean canCreate(K key) {
        return factory.canCreate(key);
      }

      public synchronized E create(K key) throws CreateException {
        return factory.create(key);
      }
    };
  }

  private static class SharedObjectFactory<K, E> implements Factory<K, E> {

    private final Factory<K, E> factory;
    private final Map<K, E> sharedObjects;

    private SharedObjectFactory(Factory<K, E> factory) {
      this.factory = factory;
      this.sharedObjects = new ConcurrentHashMap<K, E>();
    }

    private SharedObjectFactory(Factory<K, E> factory, int initialSize) {
      this.factory = factory;
      this.sharedObjects = new ConcurrentHashMap<K, E>(initialSize);
    }

    public boolean canCreate(K key) {
      return sharedObjects.containsKey(key) || factory.canCreate(key);
    }

    public E create(K key) throws CreateException {
      if (!sharedObjects.containsKey(key)) {
        sharedObjects.put(key, factory.create(key));
      }
      return sharedObjects.get(key);
    }

  }

}
