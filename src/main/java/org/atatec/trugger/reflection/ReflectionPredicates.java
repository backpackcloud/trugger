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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * An utility class for helping the use of {@link Predicate} object that involves
 * Reflection in general.
 *
 * @author Marcelo Guimarães
 */
public class ReflectionPredicates {

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element is
   * annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> Predicate<T> annotatedWith(
      final Class<? extends Annotation> annotationType) {
    return element -> element.isAnnotationPresent(annotationType);
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated element is
   * annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> Predicate<T> notAnnotatedWith(
      final Class<? extends Annotation> annotationType) {
    return ReflectionPredicates.<T>annotatedWith(annotationType).negate();
  }

  /**
   * A predicate that returns <code>true</code> if the element has annotations.
   */
  public static final Predicate<AnnotatedElement> ANNOTATED =
      element -> element.getDeclaredAnnotations().length > 0;

  /**
   * A predicate that returns <code>false</code> if the element has that annotation.
   */
  public static final Predicate<AnnotatedElement> NOT_ANNOTATED = ANNOTATED.negate();

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has a
   * name that with the given one.
   */
  public static <T extends Member> Predicate<T> named(final String name) {
    return element -> element.getName().equals(name);
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has the
   * specified modifiers.
   */
  public static <T extends Member> Predicate<T> declare(int... modifiers) {
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
   * @return a predicate that returns <code>false</code> if the evaluated element does not
   * have the specified modifiers.
   */
  public static <T extends Member> Predicate<T> dontDeclare(int... modifiers) {
    return element -> {
      int elModifiers = element.getModifiers();
      for (int mod : modifiers) {
        if ((elModifiers & mod) != 0) {
          return false;
        }
      }
      return true;
    };
  }

  public static <T extends Member> Predicate<T> nonStatic() {
    return dontDeclare(Modifier.STATIC);
  }

}
