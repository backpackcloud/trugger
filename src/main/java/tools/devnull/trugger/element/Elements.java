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

import tools.devnull.trugger.Selection;
import tools.devnull.trugger.element.impl.TruggerElementSelection;
import tools.devnull.trugger.util.ImplementationLoader;
import tools.devnull.trugger.util.OptionalFunction;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A class for helping {@link Element} selection.
 *
 * @author Marcelo "Ataxexe" Guimarães.
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
   * Registers the given finder
   *
   * @see ElementFactory#register(ElementFinder, Predicate)
   */
  public static void register(ElementFinder finder, Predicate<Class> predicate) {
    factory.register(finder, predicate);
  }

  public static ElementSelection select() {
    return new TruggerElementSelection(factory);
  }

  /**
   * Short for {@code select().element(name)}
   */
  public static ElementSelector element(String name) {
    return select().element(name);
  }

  /**
   * Short for {@code select().elements()}
   */
  public static ElementsSelector elements() {
    return select().elements();
  }

  /**
   * Short for {@code select().element()}
   */
  public static ElementSelector element() {
    return select().element();
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

  /**
   * Returns a function that gets the value of a selected element.
   *
   * @return a function that gets the value of a selected element.
   */
  public static <E> OptionalFunction<Selection<Element>, E> getValue() {
    return OptionalFunction.of(selection -> selection.result().getValue());
  }

  /**
   * Returns a function that sets the value of a selected element.
   *
   * @param newValue the value to set
   * @return a function that sets the value of a selected element.
   */
  public static Consumer<Selection<Element>> setValue(Object newValue) {
    return selection -> selection.result().setValue(newValue);
  }

}
