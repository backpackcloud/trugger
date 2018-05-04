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
package tools.devnull.trugger.element;

import tools.devnull.trugger.reflection.ReflectionPredicates;
import tools.devnull.trugger.util.Utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * An utility class for helping the use of {@link Predicate predicates} for
 * {@link Element elements}.
 *
 * @author Marcelo "Ataxexe" Guimarães
 * @since 1.2
 */
public interface ElementPredicates {

  /**
   * @return a predicate that returns <code>true</code> if an element is of the
   * given type.
   * @since 2.0
   */
  static Predicate<Element> ofType(Class<?> type) {
    return element -> type.equals(element.type());
  }

  /**
   * @see ReflectionPredicates#annotatedWith(Class)
   */
  static Predicate<Element> annotatedWith(
      Class<? extends Annotation> annotationType) {
    return ReflectionPredicates.annotatedWith(annotationType);
  }

  /**
   * A predicate that returns <code>true</code> if the element has annotations.
   */
  static Predicate<Element> annotated() {
    return element -> element.getDeclaredAnnotations().length > 0;
  }

  /**
   * @return a predicate that returns <code>true</code> if the element name
   * equals one of the specified names.
   */
  static Predicate<Element> ofName(String... elementNames) {
    Arrays.sort(elementNames);
    return element -> Arrays.binarySearch(elementNames, element.name()) >= 0;
  }

  /**
   * A predicate that returns <code>true</code> if the element is writable.
   */
  static Predicate<Element> writable() {
    return Element::isWritable;
  }

  /**
   * A predicate that returns <code>true</code> if the element is readable.
   */
  static Predicate<Element> readable() {
    return Element::isReadable;
  }

  /**
   * @return a predicate that return <code>true</code> if the element is
   * assignable to the given type.
   */
  static Predicate<Element> assignableTo(Class<?> type) {
    return element -> Utils.areAssignable(type, element.type());
  }

  /**
   * A predicate that returns <code>true</code> if the element is
   * {@link Element#isSpecific() specific}.
   */
  static Predicate<Element> specific() {
    return Element::isSpecific;
  }

}
