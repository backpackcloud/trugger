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
import io.backpackcloud.trugger.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A default implementation for an Element finder.
 *
 * @author Marcelo Guimaraes
 */
public final class TruggerElementFinder implements ElementFinder {

  private final Set<ElementFinder> registry;
  private final ElementFinder defaultFinder;

  TruggerElementFinder(ElementFinder defaultFinder,
                              Set<ElementFinder> registry) {
    this.registry = registry;
    this.defaultFinder = defaultFinder;
  }

  @Override
  public boolean canFind(Class type) {
    return true;
  }

  @Override
  public Optional<Element> find(String name, Object target) {
    if (name.indexOf('.') > -1) {
      return Optional.ofNullable(NestedElement.createNestedElement(target, name));
    }
    Class type = Utils.resolveType(target);
    return registry.stream()
        .filter(finder -> finder.canFind(type))
        .findFirst()
        .orElse(defaultFinder)
        .find(name, target);
  }

  @Override
  public List<Element> findAll(Object target) {
    Class type = Utils.resolveType(target);
    return registry.stream()
        .filter(finder -> finder.canFind(type))
        .findFirst()
        .orElse(defaultFinder)
        .findAll(target);
  }

}
