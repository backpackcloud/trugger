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
package net.sf.trugger.factory;

import net.sf.trugger.CreateException;

/**
 * Interface that defines a factory that, based on a key, creates an object.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <K>
 *          the key type
 * @param <E>
 *          the object type
 */
public interface Factory<K, E> {
  
  /**
   * Creates and returns an object based on the specified key.
   * 
   * @param key
   *          the key to create the object
   * @return the created object
   * @throws CreateException
   *           if an error occurs while creating the object.
   */
  E create(K key) throws CreateException;
  
  /**
   * Check if this factory can create an object based on the passed key.
   * 
   * @param key
   *          the key to create an object
   * @return <code>true</code> if an object can be created based on the
   *         specified key.
   */
  boolean canCreate(K key);
  
}
