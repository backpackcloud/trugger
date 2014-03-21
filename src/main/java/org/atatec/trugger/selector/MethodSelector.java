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

import org.atatec.trugger.PredicableResult;
import org.atatec.trugger.reflection.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for a single {@link Method} object assuming that the
 * name was specified before.
 *
 * @author Marcelo Guimarães
 */
public interface MethodSelector extends MethodSpecifier, PredicableResult<Method, Object> {

  MethodSelector annotated();

  MethodSelector notAnnotated();

  MethodSelector annotatedWith(Class<? extends Annotation> type);

  MethodSelector notAnnotatedWith(Class<? extends Annotation> type);

  MethodSelector nonStatic();

  MethodSelector nonFinal();

  MethodSelector that(Predicate<? super Method> predicate);

  MethodSelector recursively();

  MethodSelector returning(Class<?> returnType);

  MethodSelector withoutReturnType();

  MethodSelector withParameters(Class<?>... parameterTypes);

  MethodSelector withoutParameters();

  /**
   * Selects the single method matching the previously specified selectors.
   * <p/>
   * This method may throw a {@link ReflectionException} if the specified selectors
   * doesn't take to a single method in the given target.
   *
   * @since 2.1
   */
  Method in(Object target) throws ReflectionException;

}
