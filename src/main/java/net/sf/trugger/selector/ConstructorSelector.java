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
import java.lang.reflect.Constructor;

import net.sf.trugger.Result;
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionException;

/**
 * Interface that defines a selector for a single {@link Constructor} object.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface ConstructorSelector extends AnnotatedElementSelector, PredicateSelector<Constructor<?>>,
    AccessSelector, Predicable<Constructor<?>>, Result<Constructor<?>, Object> {
  
  ConstructorSelector withAccess(Access access);
  
  ConstructorSelector annotated();
  
  ConstructorSelector notAnnotated();
  
  ConstructorSelector annotatedWith(Class<? extends Annotation> type);
  
  ConstructorSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  ConstructorSelector that(Predicate<? super Constructor<?>> predicate);
  
  /**
   * Selects the constructor that takes the specified parameter types.
   * 
   * @param parameterTypes
   *          the parameter types taken by the constructor.
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
   * <p>
   * This method may throw a {@link ReflectionException} if the specified
   * selectors doesn't take to a single constructor in the given target.
   *
   * @since 2.1
   */
  Constructor<?> in(Object target) throws ReflectionException;
  
}
