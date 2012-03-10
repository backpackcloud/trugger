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
import java.util.Set;

import net.sf.trugger.Result;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.predicate.Predicate;

/**
 * Interface that defines a selector for {@link Element} objects.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface ElementsSelector extends ElementSpecifier, Result<Set<Element>, Object> {
  
  ElementsSelector annotated();
  
  ElementsSelector notAnnotated();
  
  ElementsSelector annotatedWith(Class<? extends Annotation> type);
  
  ElementsSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  ElementsSelector ofType(Class<?> type);
  
  ElementsSelector assignableTo(Class<?> type);
  
  ElementsSelector thatMatches(Predicate<? super Element> predicate);
  
  ElementsSelector readable();
  
  ElementsSelector nonReadable();
  
  ElementsSelector writable();
  
  ElementsSelector nonWritable();
  
  ElementsSelector nonSpecific();
  
  ElementsSelector specific();
  
  /**
   * Selects the elements that have one of the specified names.
   * 
   * @return a reference to this object.
   */
  ElementsSelector named(String... names);
  
  /**
   * Selects the elements for binding a value.
   * <p>
   * The target passed for the Result object must be the target for binding. If
   * the element is static (like a static field), the target must be a
   * {@link Class}.
   * 
   * @return the component for getting the elements
   */
  Result<Set<BindableElement>, Object> forBind();
  
}
