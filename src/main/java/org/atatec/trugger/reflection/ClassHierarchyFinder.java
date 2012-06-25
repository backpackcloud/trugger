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
package org.atatec.trugger.reflection;

import org.atatec.trugger.util.ClassIterator;

/**
 * A helper for doing searches in a class hierarchy.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The result type
 */
public abstract class ClassHierarchyFinder<E> {
  
  /**
   * A common result for indicating that no value is found. This variable should
   * be used to improve the code readability.
   */
  protected final E NONE = null;
  
  /**
   * Finds an object using the given class.
   * 
   * @param clazz
   *          the current class in the hierarchy.
   * @return the found object or null if no one is found.
   */
  protected abstract E findObject(Class<?> clazz);
  
  /**
   * @return the default return if the object is not found after the iteration
   *         over the class hierarchy.
   */
  protected E noFind() {
    return NONE;
  }
  
  /**
   * Finds an object in the hierarchy of given target.
   * 
   * @return the found object
   */
  public E find(Object target) {
    ClassIterator it = new ClassIterator(target);
    while (it.hasNext()) {
      E o = findObject(it.next());
      if (NONE != o) {
        return o;
      }
    }
    return noFind();
  }
}
