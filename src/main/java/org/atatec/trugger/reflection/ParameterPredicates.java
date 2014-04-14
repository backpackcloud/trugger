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
import java.lang.reflect.Parameter;
import java.util.function.Predicate;

/**
 * A class that holds a set of predicates to evaluate parameters.
 *
 * @since 5.0
 */
public final class ParameterPredicates {

  private ParameterPredicates() {
  }

  /**
   * Returns a predicate that accepts parameters of the given type.
   */
  public static Predicate<Parameter> type(Class type) {
    return parameter -> parameter.getType().equals(type);
  }

  /**
   * Returns a predicate that accepts parameters with the given name.
   * <p>
   * Note that the code must be compiled with <code>-parameters</code> or
   * the parameter names will be in a argX format.
   */
  public static Predicate<Parameter> name(String name) {
    return parameter -> parameter.getName().equals(name);
  }

  /**
   * Returns a predicate that accepts parameters annotated with the given
   * annotation type.
   */
  public static Predicate<Parameter> annotatedWith(Class<? extends Annotation> type) {
    return parameter -> parameter.isAnnotationPresent(type);
  }

}
