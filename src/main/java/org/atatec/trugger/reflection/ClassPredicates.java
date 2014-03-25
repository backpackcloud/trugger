/*
 * Copyright 2009-2014 Marcelo Guimarães
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

import org.atatec.trugger.element.Element;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * A set of predicates to use with <code>Class</code> objects.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public class ClassPredicates {

  private ClassPredicates() {
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>interface</i> and is not
   * an <i>annotation</i>.
   */
  public static final Predicate<Class> INTERFACE =
      ClassPredicates.declare(Modifier.INTERFACE).and(notAssignableTo(Annotation.class));
  /**
   * The negation of the {@link #INTERFACE} predicate.
   */
  public static final Predicate<Class> NOT_INTERFACE = INTERFACE.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>enum</i>.
   */
  public static final Predicate<Class> ENUM = element -> element.isEnum();

  /**
   * The negation of the {@link #ENUM} predicate.
   */
  public static final Predicate<Class> NOT_ENUM = ENUM.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>annotation</i>.
   */
  public static final Predicate<Class> ANNOTATION =
      ClassPredicates.declare(Modifier.INTERFACE)
          .and(assignableTo(Annotation.class));
  /**
   * The negation of the {@link #ANNOTATION} predicate.
   */
  public static final Predicate<Class> NOT_ANNOTATION = ANNOTATION.negate();
  /**
   * Predicate that returns <code>true</code> if a class is not an <i>interface</i> and is
   * not an <i>enum</i>.
   */
  public static final Predicate<Class> CLASS = NOT_INTERFACE.and(NOT_ENUM).and(NOT_ANNOTATION);

  /**
   * @return a predicate that returns <code>false</code> if the evaluated class has the
   * specified modifiers.
   */
  public static Predicate<Class> declare(final int... modifiers) {
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
   * @return a predicate that returns <code>false</code> if the evaluated class does not
   * have the specified modifiers.
   */
  public static Predicate<Class> dontDeclare(int... modifiers) {
    return declare(modifiers).negate();
  }

  /**
   * @return a predicate that checks if the given element is an array.
   * @since 4.1
   */
  public static Predicate<Element> ARRAY = element -> element.type().isArray();

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is
   * assignable from the evaluated element.
   */
  public static Predicate<Class> assignableTo(Class clazz) {
    return element -> clazz.isAssignableFrom(element);
  }

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is not
   * assignable from the evaluated element.
   */
  public static Predicate<Class> notAssignableTo(final Class clazz) {
    return assignableTo(clazz).negate();
  }

}
