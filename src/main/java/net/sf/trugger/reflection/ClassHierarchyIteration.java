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
package net.sf.trugger.reflection;

import net.sf.trugger.util.Utils;

/**
 * A helper class for doing iterations over a class hierarchy.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class ClassHierarchyIteration {
  
  /**
   * Executes the iteration action over the given class found in the hierarchy.
   * 
   * @param clazz
   *          the class found in the hierarchy.
   */
  protected abstract void iteration(Class<?> clazz);
  
  /**
   * Executes the iteration over the hierarchy of given target.
   * 
   * @param target
   *          the target to start the iteration.
   */
  public final void iterate(Object target) {
    Class<?> clazz = Utils.resolveType(target);
    for (Class<?> type = clazz ; type != null ; type = type.getSuperclass()) {
      iteration(type);
    }
  }
}
