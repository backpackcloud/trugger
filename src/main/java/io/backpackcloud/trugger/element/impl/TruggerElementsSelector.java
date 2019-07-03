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
import io.backpackcloud.trugger.element.ElementFinder;
import io.backpackcloud.trugger.element.ElementsSelector;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A default implementation for {@link ElementsSelector}.
 *
 * @author Marcelo Guimaraes
 */
public final class TruggerElementsSelector implements ElementsSelector {

  private final Predicate<? super Element> predicate;
  private final ElementFinder finder;

  public TruggerElementsSelector(ElementFinder finder) {
    this.finder = finder;
    this.predicate = null;
  }

  public TruggerElementsSelector(ElementFinder finder,
                                 Predicate<? super Element> predicate) {
    this.predicate = predicate;
    this.finder = finder;
  }

  @Override
  public ElementsSelector filter(Predicate<? super Element> predicate) {
    return new TruggerElementsSelector(finder, predicate);
  }

  public List<Element> from(Object target) {
    List<Element> elements = finder.findAll(target);
    if (predicate != null) {
      return elements.stream().filter(predicate).collect(Collectors.toList());
    }
    return elements;
  }

}
