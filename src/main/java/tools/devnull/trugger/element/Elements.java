/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.element;

import tools.devnull.trugger.Finder;
import tools.devnull.trugger.util.ImplementationLoader;
import tools.devnull.trugger.util.registry.Registry;
import tools.devnull.trugger.selector.ElementSelector;
import tools.devnull.trugger.selector.ElementsSelector;

import java.util.function.Predicate;

/**
 * A class for helping {@link Element} selection.
 *
 * @author Marcelo Guimarães.
 * @since 1.2
 */
public class Elements {

  private static final ElementFactory factory;

  private Elements() {
  }

  static {
    factory = ImplementationLoader.get(ElementFactory.class);
  }

  /**
   * @return the registry.
   * @since 2.3
   */
  public static Registry<Predicate<Class>, Finder<Element>> registry() {
    return factory.registry();
  }

  /**
   * Selects an element.
   *
   * @param name the element name.
   * @return a component for selecting the element.
   */
  public static ElementSelector element(String name) {
    return factory.createElementSelector(name);
  }

  /**
   * Selects a set of elements.
   *
   * @return a component for selecting the elements.
   */
  public static ElementsSelector elements() {
    return factory.createElementsSelector();
  }

  /**
   * Selects a single element.
   *
   * @return a component for selecting the element.
   */
  public static ElementSelector element() {
    return new SingleElementSelector();
  }

  /**
   * Copies elements through objects.
   */
  public static ElementCopier copy() {
    return factory.createElementCopier();
  }

  /**
   * Copies the elements returned by the given selector through objects.
   */
  public static ElementCopier copy(ElementsSelector selector) {
    return factory.createElementCopier(selector);
  }

}
