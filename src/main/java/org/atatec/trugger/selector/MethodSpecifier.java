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
package org.atatec.trugger.selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Base interface for selecting {@link Method} objects.
 * 
 * @author Marcelo Guimarães
 * @since 2.1
 */
public interface MethodSpecifier extends MemberSelector<Method> {
  
  MethodSpecifier annotated();
  
  MethodSpecifier annotatedWith(Class<? extends Annotation> type);
  
  MethodSpecifier nonFinal();
  
  MethodSpecifier nonStatic();
  
  MethodSpecifier notAnnotated();
  
  MethodSpecifier notAnnotatedWith(Class<? extends Annotation> type);
  
  MethodSpecifier recursively();
  
  MethodSpecifier that(Predicate<? super Method> predicate);
  
  /**
   * Selects the method that takes the specified parameters.
   *
   * @param parameterTypes
   *          the parameter types.
   * @return the component used for selection on the target.
   */
  MethodSpecifier withParameters(Class<?>... parameterTypes);

  /**
   * Selects the method that does not take any parameter.
   *
   * @return the component used for selection on the target.
   */
  MethodSpecifier withoutParameters();
  
  /**
   * Selects methods that returns the specified type.
   * 
   * @param returnType
   *          the return type of the methods to select.
   * @return a reference for this object.
   */
  MethodSpecifier returning(Class<?> returnType);
  
  /**
   * Selects void methods.
   * 
   * @return a reference for this object.
   */
  MethodSpecifier withoutReturnType();
  
}
