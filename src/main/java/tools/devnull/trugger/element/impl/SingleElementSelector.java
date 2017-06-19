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

package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.ElementSelector;

import java.util.List;
import java.util.function.Predicate;

import static tools.devnull.trugger.element.Elements.elements;

/**
 * A class to select a single element without using a name.
 *
 * @since 5.0
 */
class SingleElementSelector implements ElementSelector {

  private final Predicate<? super Element> predicate;

  SingleElementSelector() {
    this.predicate = (el) -> true;
  }

  SingleElementSelector(Predicate<? super Element> predicate) {
    this.predicate = predicate;
  }

  @Override
  public ElementSelector filter(Predicate<? super Element> predicate) {
    return new SingleElementSelector(predicate);
  }

  @Override
  public Element from(Object o) {
    List<Element> elements = elements().filter(predicate).from(o);
    return elements.isEmpty() ? null : elements.iterator().next();
  }
}
