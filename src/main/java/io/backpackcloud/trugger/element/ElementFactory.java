/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger.element;

/**
 * Interface that defines a factory for {@link Element} objects operations.
 *
 * @author Marcelo Guimaraes
 * @since 1.2
 */
public interface ElementFactory {

  /**
   * Registers the given finder to use with element finding operations
   *
   * @param finder the finder to register
   */
  void register(ElementFinder finder);

  /**
   * Creates a selector for an {@link Element} object.
   *
   * @param name the element name.
   * @return the selector.
   */
  ElementSelector createElementSelector(String name);

  /**
   * Creates a selector for a set of {@link Element} objects.
   *
   * @return the selector.
   */
  ElementsSelector createElementsSelector();

  /**
   * Creates a new ElementCopier for all elements.
   */
  ElementCopier createElementCopier();

  /**
   * Creates a new ElementCopier for the elements returned by the given selector
   */
  ElementCopier createElementCopier(ElementsSelector selector);

}
