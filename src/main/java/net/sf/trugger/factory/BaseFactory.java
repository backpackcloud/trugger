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
package net.sf.trugger.factory;

import net.sf.trugger.CreateException;
import net.sf.trugger.reflection.Reflection;

/**
 * This class provides a default implementation for a factory.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <K>
 *          the key type
 * @param <E>
 *          the object type
 */
public abstract class BaseFactory<K, E> implements Factory<K, E> {

  /**
   * Tests if the object can be created by calling the method
   * {@link #canCreate(Object)}, if the return is <code>true</code>, the
   * {@link #instantiate(Object, Class)} method will be used to create the
   * object, if the return is <code>false</code>, the method calls the method
   * {@link #defaultReturn(Object)}.
   */
  public final E create(K key) throws CreateException {
    E object;
    if (!canCreate(key)) {
      object = defaultReturn(key);
    } else {
      try {
        object = instantiate(key, resolveClassForCreation(key));
      } catch (Throwable e) {
        throw new CreateException(e);
      }
    }
    return object;
  }

  /**
   * Instantiates the class of the given type.
   *
   * @param key
   *          the key passed to this factory.
   * @param classToCreate
   *          the type for creation.
   * @return the created object.
   * @throws Throwable
   *           if an error occurs.
   */
  protected E instantiate(K key, Class<? extends E> classToCreate) throws Throwable {
    return Reflection.newInstanceOf(classToCreate);
  }

  /**
   * This method is call when a object cannot be created (if the method
   * {@link #canCreate(Object)} returned <code>false</code>).
   * <p>
   * This implementation throws a {@link CreateException}
   *
   * @param key
   *          the key passed to create the object
   * @return the default object to return if no one can be created.
   */
  protected E defaultReturn(K key) {
    throw new CreateException("The object cannot be created");
  }

  /**
   * Returns the object class that must be created.
   *
   * @param key
   *          the key passed to the method {@link #create(Object)}
   * @return the object class that must be created.
   */
  protected abstract Class<? extends E> resolveClassForCreation(K key);

}
