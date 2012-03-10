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
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;

/**
 * Interface that defines a selector for setter methods.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.1
 */
public interface SetterMethodSelector extends AnnotatedElementSelector, PredicateSelector<Method>,
    Result<Set<Method>, Object>, RecursionSelector, Predicable<Method> {

  SetterMethodSelector thatMatches(Predicate<? super Method> predicate);

  SetterMethodSelector annotated();
  
  SetterMethodSelector notAnnotated();
  
  SetterMethodSelector annotatedWith(Class<? extends Annotation> type);

  SetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  /**
   * Note: this selection does not affect the predicate returned by
   * {@link #toPredicate()}.
   */
  SetterMethodSelector recursively();

  /**
   * Selects the setter method that receives a value assignable to the given
   * type.
   *
   * @param type
   *          the argument type in the setter method.
   * @return the component for selecting the method.
   */
  Result<Method, Object> forType(Class<?> type);

}
