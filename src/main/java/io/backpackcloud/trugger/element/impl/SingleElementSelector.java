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

package io.backpackcloud.trugger.element.impl;

import io.backpackcloud.trugger.element.Element;
import io.backpackcloud.trugger.element.ElementSelector;
import io.backpackcloud.trugger.element.Elements;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
  public Optional<Element> from(Object target) {
    List<Element> elements = Elements.elements().filter(predicate).from(target);
    return elements.isEmpty() ?
        Optional.empty() :
        Optional.of(elements.iterator().next());
  }
}
