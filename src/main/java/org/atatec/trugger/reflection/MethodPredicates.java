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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * A set of predicates to use with <code>Method</code> objects.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public class MethodPredicates {

  private MethodPredicates() {
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         specified type as the return type.
   */
  public static Predicate<Method> returns(Class returnType) {
    return element -> element.getReturnType().equals(returnType);
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         return type assignable to the specified type.
   */
  public static Predicate<Method> returnsAssignableTo(Class returnType) {
    return element -> returnType.isAssignableFrom(element.getReturnType());
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method takes the
   *         specified parameters.
   */
  public static Predicate<Method> takes(Class... parameterTypes) {
    return element -> Arrays.equals(element.getParameterTypes(), parameterTypes);
  }

}
