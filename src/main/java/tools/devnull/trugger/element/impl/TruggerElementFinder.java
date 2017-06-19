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

import tools.devnull.trugger.Optional;
import tools.devnull.trugger.element.ElementFinder;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.util.Utils;
import tools.devnull.trugger.util.registry.Registry;

import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation for an Element finder.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementFinder implements ElementFinder {

  private final Registry<Predicate<Class>, ElementFinder> registry;
  private final ElementFinder defaultFinder;

  public TruggerElementFinder(ElementFinder defaultFinder,
                              Registry<Predicate<Class>, ElementFinder> registry) {
    this.registry = registry;
    this.defaultFinder = defaultFinder;
  }

  private ElementFinder getFinder(Object target) {
    Class type = Utils.resolveType(target);
    for (Registry.Entry<Predicate<Class>, ElementFinder> entry : registry.entries()) {
      if (entry.key().test(type)) {
        return entry.value();
      }
    }
    return defaultFinder;
  }

  @Override
  public Optional<Element> find(String name, Object target) {
    if (name.indexOf('.') > -1) {
      return Optional.of(NestedElement.createNestedElement(target, name));
    }
    ElementFinder finder = getFinder(target);
    return finder.find(name, target);
  }

  @Override
  public List<Element> findAll(Object target) {
    ElementFinder finder = getFinder(target);
    return finder.findAll(target);
  }

}
