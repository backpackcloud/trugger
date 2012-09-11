/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.atatec.trugger.predicate.Predicates.is;

/**
 * A set of predicates to use with <code>Method</code> objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public class MethodPredicates {

  private MethodPredicates() {
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         specified type as the return type.
   */
  public static CompositePredicate<Method> returns(final Class returnType) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getReturnType().equals(returnType);
      }

      @Override
      public String toString() {
        return "Method returning " + returnType.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         return type assignable to the specified type.
   */
  public static CompositePredicate<Method> returnsAssignableTo(final Class returnType) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return returnType.isAssignableFrom(element.getReturnType());
      }

      @Override
      public String toString() {
        return "Method returning assignable to " + returnType.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method takes the
   *         specified parameters.
   */
  public static CompositePredicate<Method> takes(final Class... parameterTypes) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return Arrays.equals(element.getParameterTypes(), parameterTypes);
      }

      @Override
      public String toString() {
        return "With parameters " + Arrays.toString(parameterTypes);
      }
    });
  }

  public static CompositePredicate<Method> HAS_DEFAULT_VALUE = is(new Predicate<Method>() {

    public boolean evaluate(Method element) {
      return element.getDefaultValue() != null;
    }

    @Override
    public String toString() {
      return "With default value";
    }
  });

}
