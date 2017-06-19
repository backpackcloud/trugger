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

import tools.devnull.trugger.SelectionResult;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.ElementFinder;
import tools.devnull.trugger.element.ElementSelector;

import java.util.function.Predicate;

/**
 * A default implementation for {@link ElementSelector}.
 *
 * @author Marcelo Guimarães
 */
public class TruggerElementSelector implements ElementSelector {

  private final Predicate<? super Element> predicate;
  private final ElementFinder finder;
  private final String name;

  public TruggerElementSelector(String name, ElementFinder finder) {
    this.name = name;
    this.finder = finder;
    this.predicate = e -> true;
  }

  public TruggerElementSelector(String name, ElementFinder finder,
                                Predicate<? super Element> predicate) {
    this.predicate = predicate;
    this.finder = finder;
    this.name = name;
  }

  public ElementSelector filter(Predicate<? super Element> predicate) {
    return new TruggerElementSelector(name, finder, predicate);
  }

  public SelectionResult<Element> from(Object target) {
    Element element = finder.find(name, target).filter(predicate).value();
    return new SelectionResult<>(target, element);
  }

}
