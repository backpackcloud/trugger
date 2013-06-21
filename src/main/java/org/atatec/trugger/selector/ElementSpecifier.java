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

import org.atatec.trugger.element.Element;
import org.atatec.trugger.predicate.Predicate;

import java.lang.annotation.Annotation;

/**
 * Base interface for selecting {@link Element elements}.
 *
 * @author Marcelo Guimarães
 * @since 2.0
 */
public interface ElementSpecifier extends AnnotatedElementSelector, PredicateSelector<Element>,
    TypedElementSelector {

  /**
   * Selects readable elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier readable();

  /**
   * Selects non-readable elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier nonReadable();

  /**
   * Selects writable elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier writable();

  /**
   * Selects non-writable elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier nonWritable();

  /**
   * Selects specific elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier specific();

  /**
   * Selects non-specific elements.
   *
   * @return a reference to this object.
   */
  ElementSpecifier nonSpecific();

  ElementSpecifier annotated();
  
  ElementSpecifier notAnnotated();
  
  ElementSpecifier annotatedWith(Class<? extends Annotation> type);

  ElementSpecifier notAnnotatedWith(Class<? extends Annotation> type);

  ElementSpecifier ofType(Class<?> type);
  
  ElementSpecifier assignableTo(Class<?> type);

  ElementSpecifier that(Predicate<? super Element> predicate);

}
