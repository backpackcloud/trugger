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

import io.backpackcloud.trugger.element.ElementCopier;
import io.backpackcloud.trugger.element.ElementFactory;
import io.backpackcloud.trugger.element.ElementFinder;
import io.backpackcloud.trugger.element.ElementSelector;
import io.backpackcloud.trugger.element.ElementsSelector;

import java.util.HashSet;
import java.util.Set;

/**
 * A default implementation for ElementFactory.
 *
 * @author Marcelo Guimaraes
 */
public final class TruggerElementFactory implements ElementFactory {

  private ElementFinder finder;
  private final Set<ElementFinder> registry;

  public TruggerElementFactory() {
    registry = new HashSet<>();
    registry.add(new AnnotationElementFinder());
    registry.add(new PropertiesElementFinder());
    registry.add(new ResourceBundleElementFinder());
    registry.add(new ResultSetElementFinder());
    registry.add(new MapElementFinder());
    registry.add(new ArrayElementFinder());
    registry.add(new ListElementFinder());
    finder = new TruggerElementFinder(new ObjectElementFinder(), registry);
  }

  @Override
  public void register(ElementFinder finder) {
    this.registry.add(finder);
  }

  /**
   * Returns a new {@link TruggerElementSelector}.
   */
  public ElementSelector createElementSelector(String name) {
    return new TruggerElementSelector(name, finder);
  }

  /**
   * Returns a new {@link TruggerElementsSelector}.
   */
  public ElementsSelector createElementsSelector() {
    return new TruggerElementsSelector(finder);
  }

  @Override
  public ElementCopier createElementCopier() {
    return new TruggerElementCopier();
  }

  @Override
  public ElementCopier createElementCopier(ElementsSelector selector) {
    return new TruggerElementCopier(selector);
  }

}
