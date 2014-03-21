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

import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for {@link Element} objects.
 * 
 * @author Marcelo Guimarães
 */
public interface ElementsSelector extends ElementSpecifier, Result<Set<Element>, Object> {
  
  ElementsSelector annotated();
  
  ElementsSelector notAnnotated();
  
  ElementsSelector annotatedWith(Class<? extends Annotation> type);
  
  ElementsSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  ElementsSelector ofType(Class<?> type);
  
  ElementsSelector assignableTo(Class<?> type);
  
  ElementsSelector that(Predicate<? super Element> predicate);
  
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

}
