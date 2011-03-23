/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element;

import net.sf.trugger.Finder;
import net.sf.trugger.registry.Registry;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementsSelector;

/**
 * Interface that defines a factory for {@link Element} objects operations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface ElementFactory {

  /**
   * Returns the registry that associates classes to finders.
   *
   * @return the registry.
   * @since 2.3
   */
  Registry<Class<?>, Finder<Element>> registry();

  /**
   * Creates a selector for an {@link Element} object.
   *
   * @param name
   *          the element name.
   * @return the selector.
   */
  ElementSelector createElementSelector(String name);

  /**
   * Creates a selector for an {@link Element} object without specifying the
   * name.
   *
   * @return the selector.
   * @since 2.5
   */
  ElementSelector createElementSelector();

  /**
   * Creates a selector for a set of {@link Element} objects.
   *
   * @return the selector.
   */
  ElementsSelector createElementsSelector();

  /**
   * Creates the component for the element copy operation.
   *
   * @param dest
   *          the destination object.
   * @return the created component
   */
  ElementCopier createElementCopier(Object dest);

}
