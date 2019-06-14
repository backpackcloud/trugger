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

package io.backpackcloud.trugger.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * A set of predicates to use with <code>Class</code> objects.
 *
 * @author Marcelo "Ataxexe" Guimarães
 * @since 4.1
 */
public interface ClassPredicates {

  /**
   * Returns a predicate that checks if a class is of the given type.
   */
  static Predicate<Class> ofType(Class type) {
    return c -> c.equals(type);
  }

  /**
   * Returns a predicate that checks if a class is of a primitive type.
   *
   * @since 5.1
   */
  static Predicate<Class> primitiveType() {
    return ofType(int.class)
        .or(ofType(double.class))
        .or(ofType(boolean.class))
        .or(ofType(char.class))
        .or(ofType(long.class))
        .or(ofType(float.class))
        .or(ofType(short.class))
        .or(ofType(byte.class));
  }

  /**
   * Returns a predicate that checks if a class is of a primitive array type.
   *
   * @since 5.1
   */
  static Predicate<Class> primitiveArrayType() {
    return arrayType().and(type -> primitiveType().test(type.getComponentType()));
  }

  /**
   * Returns a predicate that checks if a class is a subtype of another class
   */
  static Predicate<Class> subtypeOf(Class type) {
    return c -> !c.equals(type) && type.isAssignableFrom(c);
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>interface</i>
   * and is not an <i>annotation</i>.
   */
  static Predicate<Class> interfaceType() {
    return declared(Modifier.INTERFACE).and(assignableTo(Annotation.class).negate());
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>enum</i>.
   */
  static Predicate<Class> enumType() {
    return Class::isEnum;
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>annotation</i>.
   */
  static Predicate<Class> annotationType() {
    return declared(Modifier.INTERFACE).and(assignableTo(Annotation.class));
  }

  /**
   * Predicate that returns <code>true</code> if a class has the class keyword in its declaration.
   */
  static Predicate<Class> classType() {
    return interfaceType().or(enumType()).or(annotationType()).negate();
  }

  /**
   * A predicate that checks if the given element is an array.
   *
   * @since 4.1
   */
  static Predicate<Class> arrayType() {
    return Class::isArray;
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated class has the
   * specified modifiers.
   */
  static Predicate<Class> declared(final int... modifiers) {
    return element -> {
      int elModifiers = element.getModifiers();
      for (int mod : modifiers) {
        if ((elModifiers & mod) != 0) {
          return true;
        }
      }
      return false;
    };
  }

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is
   * assignable from the evaluated element.
   */
  static Predicate<Class> assignableTo(Class clazz) {
    return element -> clazz.isAssignableFrom(element);
  }

}
