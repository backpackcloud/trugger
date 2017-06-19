/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.function.Predicate;

/**
 * An utility class for helping the use of {@link Predicate} object that involves
 * Reflection in general.
 *
 * @author Marcelo "Ataxexe" Guimarães
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
   * A predicate that returns <code>true</code> if the element has annotations.
   */
  public static final Predicate<AnnotatedElement> annotated() {
    return element -> element.getDeclaredAnnotations().length > 0;
  }

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
  public static <T extends Member> Predicate<T> declaring(int... modifiers) {
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

}
