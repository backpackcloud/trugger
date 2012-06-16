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

import net.sf.trugger.Result;
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;

/**
 * Interface that defines a selector for getter methods associated with a field.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface FieldGetterMethodSelector extends AnnotatedElementSelector, PredicateSelector<Method>,
    Result<Method, Object>, Predicable<Method> {
  
  FieldGetterMethodSelector annotated();
  
  FieldGetterMethodSelector notAnnotated();
  
  FieldGetterMethodSelector annotatedWith(Class<? extends Annotation> type);
  
  FieldGetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  FieldGetterMethodSelector that(Predicate<? super Method> predicate);
  
}
