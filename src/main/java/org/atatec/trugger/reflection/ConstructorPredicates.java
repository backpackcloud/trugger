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
import java.lang.reflect.Constructor;
import java.util.function.Predicate;

/**
 * A set of predicates to use with <code>Constructor</code> objects.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public class ConstructorPredicates {

  private ConstructorPredicates() {
  }

  /**
   * @return a predicate that returns <code>true</code> if the method has
   * annotations.
   *
   * @since 5.0
   */
  public static final Predicate<Constructor> annotated() {
    return c -> c.getDeclaredAnnotations().length > 0;
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element
   * is annotated with the specified Annotation.
   *
   * @since 5.0
   */
  public static Predicate<Constructor> annotatedWith(
      Class<? extends Annotation> annotationType) {
    return c -> c.isAnnotationPresent(annotationType);
  }

}
