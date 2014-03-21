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

package org.atatec.trugger.reflection;

import org.atatec.trugger.util.Utils;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * A set of predicates to use with <code>Field</code> objects.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public class FieldPredicates {

  private FieldPredicates() {
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated field is of the
   *         given type.
   */
  public static Predicate<Field> ofType(final Class type) {
    return element -> type.equals(element.getType());
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated field is
   *         assignable to the given type.
   */
  public static Predicate<Field> assignableTo(final Class type) {
    return element -> Utils.areAssignable(type, element.getType());
  }

}
