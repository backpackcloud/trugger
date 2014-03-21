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
package org.atatec.trugger.selector;

import org.atatec.trugger.Result;
import org.atatec.trugger.reflection.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for a single {@link Constructor} object.
 *
 * @author Marcelo Guimarães
 */
public interface ConstructorSelector extends AnnotatedElementSelector, PredicateSelector<Constructor<?>>,
  Result<Constructor<?>, Object> {

  ConstructorSelector annotated();

  ConstructorSelector notAnnotated();

  ConstructorSelector annotatedWith(Class<? extends Annotation> type);

  ConstructorSelector notAnnotatedWith(Class<? extends Annotation> type);

  ConstructorSelector that(Predicate<? super Constructor<?>> predicate);

  /**
   * Selects the constructor that takes the specified parameter types.
   *
   * @param parameterTypes the parameter types taken by the constructor.
   *
   * @return the component used for selection on the target.
   */
  ConstructorSelector withParameters(Class<?>... parameterTypes);

  /**
   * Selects the constructor that takes no parameters.
   *
   * @return the component used for selection on the target.
   */
  ConstructorSelector withoutParameters();

  /**
   * Selects the single constructor matching the previously specified selectors.
   * <p/>
   * This method may throw a {@link ReflectionException} if the specified selectors
   * doesn't take to a single constructor in the given target.
   *
   * @since 2.1
   */
  Constructor<?> in(Object target) throws ReflectionException;

}
