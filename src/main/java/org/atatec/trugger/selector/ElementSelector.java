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

import java.lang.annotation.Annotation;

import org.atatec.trugger.Result;
import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.predicate.Predicate;

/**
 * Interface that defines a selector for {@link Element} objects.
 * 
 * @author Marcelo Guimarães
 */
public interface ElementSelector extends ElementSpecifier, Result<Element, Object> {
  
  ElementSelector readable();
  
  ElementSelector nonReadable();
  
  ElementSelector writable();
  
  ElementSelector nonWritable();
  
  ElementSelector annotated();
  
  ElementSelector notAnnotated();
  
  ElementSelector annotatedWith(Class<? extends Annotation> type);
  
  ElementSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  ElementSelector ofType(Class<?> type);
  
  ElementSelector assignableTo(Class<?> type);
  
  ElementSelector that(Predicate<? super Element> predicate);
  
  ElementSelector specific();
  
  ElementSelector nonSpecific();
  
  /**
   * Selects an element for binding a value.
   * <p>
   * The target passed for the Result object must be the target for binding. If
   * the element is a static one (like a static field), the target must be a
   * {@link Class}.
   * 
   * @return the component for getting the element.
   */
  Result<BindableElement, Object> forBind();
  
}
