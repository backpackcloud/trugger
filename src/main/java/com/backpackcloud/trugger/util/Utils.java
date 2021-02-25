/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.backpackcloud.trugger.util;

import com.backpackcloud.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;

/**
 * A class with general utility methods.
 *
 * @author Marcelo Guimaraes
 * @since 1.2
 */
public interface Utils {

  /**
   * Resolves the target type.
   * <p>
   * If the target is a <code>Class</code>, it will be returned. If it is an
   * {@link Annotation}, its {@link Annotation#annotationType() type} will be
   * returned.
   * <p>
   * If none of the above, its {@link Object#getClass() class} will be returned.
   *
   * @param target the target to resolve the type
   * @return the type of the given target.
   */
  static Class<?> resolveType(Object target) {
    if (target instanceof Annotation) {
      return ((Annotation) target).annotationType();
    }
    return target instanceof Class ? ((Class<?>) target) : target.getClass();
  }

  /**
   * Given <code>c</code> the <code>object</code> class, returns the wrapper
   * class for <code>c</code> if <code>c</code> is a primitive class, returns
   * <code>c</code> otherwise.
   * <p>
   * This method should be used if the type could not be a primitive (in cases
   * of autoboxing for example). Otherwise, use the {@link #resolveType(Object)}.
   *
   * @param object the object to analyse.
   * @return the wrapper class if the object class is a primitive or the class
   * itself otherwise.
   */
  static Class<?> objectClass(Object object) {
    Class<?> c = resolveType(object);
    if (c.isPrimitive()) {
      return Reflection.wrapperFor(c);
    }
    return c;
  }

  /**
   * Checks if both classes are assignable, including the autoboxing in case of
   * a primitive class.
   *
   * @return <code>true</code> if both classes are compatible (even if an
   * autoboxing is necessary).
   */
  static boolean areAssignable(Class to, Class from) {
    if (to.isPrimitive() || from.isPrimitive()) {
      return objectClass(to).isAssignableFrom(objectClass(from));
    }
    return to.isAssignableFrom(from);
  }

}
