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

import java.util.Arrays;
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

}
