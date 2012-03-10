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
package net.sf.trugger.selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import net.sf.trugger.Result;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;

/**
 * Interface that defines a selector for {@link Method} objects.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface MethodsSelector extends MethodSpecifier, Result<Set<Method>, Object> {
  
  MethodsSelector withAccess(Access access);
  
  MethodsSelector nonStatic();
  
  MethodsSelector nonFinal();
  
  MethodsSelector thatMatches(Predicate<? super Method> predicate);
  
  MethodsSelector annotated();
  
  MethodsSelector notAnnotated();
  
  MethodsSelector annotatedWith(Class<? extends Annotation> type);
  
  MethodsSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  MethodsSelector recursively();
  
  MethodsSelector returning(Class<?> returnType);
  
  MethodsSelector withoutReturnType();
  
  MethodsSelector withParameters(Class<?>... parameterTypes);
  
  MethodsSelector withoutParameters();
  
  /**
   * Selects methods that have the specified name.
   * 
   * @return a reference to this object.
   */
  MethodsSelector named(String name);
  
}
