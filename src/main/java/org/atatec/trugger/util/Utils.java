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
package org.atatec.trugger.util;

import org.atatec.trugger.annotation.AcceptArrays;
import org.atatec.trugger.annotation.AcceptedArrayTypes;
import org.atatec.trugger.annotation.AcceptedTypes;
import org.atatec.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * A class with general utility methods.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public final class Utils {

  private Utils() {}

  /**
   * Resolves the target type.
   * <p>
   * If the target is a <code>Class</code>, it will be returned. If it is an
   * {@link Annotation}, its {@link Annotation#annotationType() type} will be
   * returned.
   * <p>
   * If none of the above, its {@link Object#getClass() class} will be returned.
   *
   * @param target
   *          the target to resolve the type
   * @return the type of the given target.
   */
  public static Class<?> resolveType(Object target) {
    if (target instanceof Annotation) {
      return ((Annotation) target).annotationType();
    }
    return target instanceof Class ? ((Class<?>) target) : target.getClass();
  }

  /**
   * Check if the specified {@link CharSequence} is empty or <code>null</code>.
   *
   * @param sequence
   *          the value to check.
   * @return <code>true</code> if the sequence has a significant content (not
   *         only spaces) or <code>false</code> if it does not.
   */
  public static boolean isEmpty(CharSequence sequence) {
    return (sequence == null) || (sequence.toString().trim().length() == 0);
  }

  /**
   * Given <code>c</code> the <code>object</code> class, returns the wrapper
   * class for <code>c</code> if <code>c</code> is a primitive class, returns
   * <code>c</code> otherwise.
   * <p>
   * This method should be used if the type could not be a primitive (in cases
   * of autoboxing for example). Otherwise, use the {@link #resolveType(Object)}.
   *
   * @param object
   *          the object to analyse.
   * @return the wrapper class if the object class is a primitive or the class
   *         itself otherwise.
   */
  public static Class<?> objectClass(Object object) {
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
   *         autoboxing is necessary).
   */
  public static boolean areAssignable(Class to, Class from) {
    if (to.isPrimitive() || from.isPrimitive()) {
      return objectClass(to).isAssignableFrom(objectClass(from));
    }
    return to.isAssignableFrom(from);
  }

  /**
   * Checks if the specified type is mapped on the given element based on the
   * annotations {@link AcceptedTypes}, {@link AcceptedArrayTypes} and
   * {@link AcceptArrays}.
   *
   * @param element
   *          the element to search for the {@link AcceptedTypes} and/or
   *          {@link AcceptedArrayTypes}.
   * @param type
   *          the type to check
   * @return <code>true</code> if the type is compatible with any type mapped to
   *         the given element.
   * @since 2.3
   */
  public static boolean isTypeAccepted(Class<?> type, AnnotatedElement element) {
    boolean array = type.isArray();
    boolean accTypes = element.isAnnotationPresent(AcceptedTypes.class);
    boolean accArrTypes = element.isAnnotationPresent(AcceptedArrayTypes.class);
    boolean accArr = element.isAnnotationPresent(AcceptArrays.class);
    if (array) {
      if (accArrTypes) {
        AcceptedArrayTypes annotation = element.getAnnotation(AcceptedArrayTypes.class);
        return checkAcceptance(type.getComponentType(), annotation.value());
      } else if (accArr) {
        return true;
      }
    }
    if (accTypes) {
      AcceptedTypes annotation = element.getAnnotation(AcceptedTypes.class);
      return checkAcceptanceWithWrapper(type, annotation.value());
    }
    return !accArr && !accArrTypes;
  }

  private static boolean checkAcceptance(Class<?> type, Class<?>[] classes) {
    for (Class<?> acceptedType : classes) {
      if (acceptedType.isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
  }

  private static boolean checkAcceptanceWithWrapper(Class<?> type, Class<?>[] classes) {
    for (Class<?> acceptedType : classes) {
      if (areAssignable(acceptedType, type)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the given objects are equals using the
   * {@link Object#equals(Object)} method or the <code>==</code> operator if the
   * first argument is <code>null</code>.
   *
   * @since 2.5
   */
  public static boolean areEquals(Object arg0, Object arg1) {
    if (arg0 != null && arg1 != null) {
      return arg0.equals(arg1);
    }
    return arg0 == arg1;
  }

}
