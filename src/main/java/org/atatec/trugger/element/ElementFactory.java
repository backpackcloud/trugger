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
package org.atatec.trugger.element;

import org.atatec.trugger.Finder;
import org.atatec.trugger.registry.Registry;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;

/**
 * Interface that defines a factory for {@link Element} objects operations.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public interface ElementFactory {

  /**
   * Returns the registry that associates classes to finders.
   *
   * @return the registry.
   *
   * @since 2.3
   */
  Registry<Class<?>, Finder<Element>> registry();

  /**
   * Creates a selector for an {@link Element} object.
   *
   * @param name the element name.
   *
   * @return the selector.
   */
  ElementSelector createElementSelector(String name);

  /**
   * Creates a selector for a set of {@link Element} objects.
   *
   * @return the selector.
   */
  ElementsSelector createElementsSelector();

  /** Creates a new ElementCopier for all elements. */
  ElementCopier createElementCopier();

  /** Creates a new ElementCopier for the elements returned by the given selector */
  ElementCopier createElementCopier(ElementsSelector selector);

}
