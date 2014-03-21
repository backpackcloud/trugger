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
package org.atatec.trugger.element;

import org.atatec.trugger.util.Utils;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * An utility class for helping the use of {@link Predicate predicates} for
 * {@link Element elements}.
 * 
 * @author Marcelo Guimarães
 * @since 1.2
 */
public final class ElementPredicates {
  
  private ElementPredicates() {}
  
  /**
   * @return a predicate that returns <code>true</code> if an element is of the
   *         given type.
   * @since 2.0
   */
  public static Predicate<Element> ofType(Class<?> type) {
    return element -> type.equals(element.type());
  }
  
  /**
   * @return a predicate that returns <code>true</code> if the element name
   *         equals one of the specified names.
   */
  public static Predicate<Element> named(String... elementNames) {
    Arrays.sort(elementNames);
    return element -> Arrays.binarySearch(elementNames, element.name()) >= 0;
  }
  
  /**
   * A predicate that returns <code>true</code> if the element is writable.
   */
  public static final Predicate<Element> WRITABLE = element -> element.isWritable();
  
  /**
   * A predicate that returns <code>false</code> if the element is writable.
   */
  public static final Predicate<Element> NON_WRITABLE = WRITABLE.negate();
  
  /**
   * A predicate that returns <code>true</code> if the element is readable.
   */
  public static final Predicate<Element> READABLE = element -> element.isReadable();
  
  /**
   * A predicate that returns <code>false</code> if the element is readable.
   */
  public static final Predicate<Element> NON_READABLE = READABLE.negate();
  
  /**
   * @return a predicate that return <code>true</code> if the element is
   *         assignable to the given type.
   */
  public static Predicate<Element> assignableTo(final Class<?> type) {
    return element -> Utils.areAssignable(type, element.type());
  }
  
  /**
   * A predicate that returns <code>true</code> if the element is
   * {@link Element#isSpecific() specific}.
   */
  public static final Predicate<Element> SPECIFIC = element -> element.isSpecific();
  
  /**
   * A predicate that returns <code>true</code> if the element is not
   * {@link Element#isSpecific() specific}.
   */
  public static final Predicate<Element> NON_SPECIFIC = SPECIFIC.negate();
  
}
