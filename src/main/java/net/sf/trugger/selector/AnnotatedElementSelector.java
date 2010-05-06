/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
import java.lang.reflect.AnnotatedElement;

/**
 * Interface that defines a selector for {@link AnnotatedElement} objects.
 * 
 * @author Marcelo Varella Barca Guimarães The element type.
 */
public interface AnnotatedElementSelector {
  
  /**
   * Selects the object annotated with any annotation.
   * 
   * @return a reference to this object.
   */
  AnnotatedElementSelector annotated();
  
  /**
   * Selects the object not annotated.
   * 
   * @return a reference to this object.
   */
  AnnotatedElementSelector notAnnotated();
  
  /**
   * Selects the object annotated with the specified annotation.
   * 
   * @param type
   *          the annotation type.
   * @return a reference to this object.
   */
  AnnotatedElementSelector annotatedWith(Class<? extends Annotation> type);
  
  /**
   * Selects the object not annotated with the specified annotation.
   * 
   * @param type
   *          the annotation type.
   * @return a reference to this object.
   */
  AnnotatedElementSelector notAnnotatedWith(Class<? extends Annotation> type);
  
}
