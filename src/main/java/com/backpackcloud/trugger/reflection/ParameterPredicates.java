/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package com.backpackcloud.trugger.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.function.Predicate;

/**
 * A class that holds a set of predicates to evaluate parameters.
 *
 * @since 5.0
 */
public interface ParameterPredicates {

  /**
   * Returns a predicate that accepts parameters of the given type.
   */
  static Predicate<Parameter> ofType(Class type) {
    return parameter -> parameter.getType().equals(type);
  }

  /**
   * Returns a predicate that accepts parameters compatible with the given type.
   *
   * @since 5.1
   */
  static Predicate<Parameter> assignableTo(Class type) {
    return parameter -> type.isAssignableFrom(parameter.getType());
  }

  /**
   * Returns a predicate that accepts parameters with the given name.
   * <p>
   * Note that the code must be compiled with <code>-parameters</code> or
   * the parameter names will be in a argX format.
   */
  static Predicate<Parameter> ofName(String name) {
    return parameter -> parameter.getName().equals(name);
  }

  /**
   * Returns a predicate that accepts parameters annotated with the given
   * annotation type.
   */
  static Predicate<Parameter> annotatedWith(Class<? extends Annotation> type) {
    return parameter -> parameter.isAnnotationPresent(type);
  }

}
