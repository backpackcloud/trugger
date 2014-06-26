/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.registry.Registry;
import org.atatec.trugger.util.Utils;

import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation for an Element finder.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementFinder implements Finder<Element> {

  private final Registry<Predicate<Class>, Finder<Element>> registry;
  private final Finder<Element> defaultFinder;

  public TruggerElementFinder(Finder<Element> defaultFinder,
                              Registry<Predicate<Class>, Finder<Element>> registry) {
    this.registry = registry;
    this.defaultFinder = defaultFinder;
  }

  private Finder<Element> getFinder(Object target) {
    Class type = Utils.resolveType(target);
    for (Registry.Entry<Predicate<Class>, Finder<Element>> entry : registry.entries()) {
      if(entry.key().test(type)) {
        return entry.value();
      }
    }
    return defaultFinder;
  }

  @Override
  public Result<Element, Object> find(final String name) {
    return target -> {
      if (name.indexOf('.') > -1) {
        return NestedElement.createNestedElement(target, name);
      }
      Finder<Element> finder = getFinder(target);
      return finder.find(name).in(target);
    };
  }

  @Override
  public Result<List<Element>, Object> findAll() {
    return target -> {
      Finder<Element> finder = getFinder(target);
      return finder.findAll().in(target);
    };
  }

}
