/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementFinderClass;
import net.sf.trugger.factory.AnnotationBasedFactory;
import net.sf.trugger.factory.Factories;
import net.sf.trugger.factory.Factory;
import net.sf.trugger.registry.Registry;
import net.sf.trugger.registry.Registry.Entry;
import net.sf.trugger.util.Utils;

/**
 * A default implementation for an Element finder.
 * <p>
 * This class uses registered finders for defined types and allows custom
 * finders via {@link ElementFinderClass}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerElementFinder implements Finder<Element> {

  private final Registry<Class<?>, Finder<Element>> registry;
  private final Finder<Element> defaultFinder;
  private final Factory<AnnotatedElement, Finder<Element>> factory;

  public TruggerElementFinder(Finder<Element> defaultFinder, Registry<Class<?>, Finder<Element>> registry) {
    this.registry = registry;
    this.factory = Factories.sharedObjectFactory(new AnnotationBasedFactory<ElementFinderClass, Finder<Element>>(ElementFinderClass.class));
    this.defaultFinder = defaultFinder;
  }

  private Finder<Element> getFinder(Object target) {
    Class<?> type = Utils.resolveType(target);
    if (factory.canCreate(type)) {
      return factory.create(type);
    }
    if (registry.hasRegistryFor(type)) {
      return registry.registryFor(type);
    }
    Class<?> superclass = type.getSuperclass();
    // trying to avoid the loop below
    if ((superclass != null) && registry.hasRegistryFor(superclass)) {
      return registry.registryFor(superclass);
    }
    for (Entry<Class<?>, Finder<Element>> entry : registry.entries()) {
      if (entry.key().isAssignableFrom(type)) {
        Finder<Element> finder = entry.registry();
        registry.register(finder).to(type);
        return finder;
      }
    }
    return defaultFinder;
  }

  @Override
  public Result<Element, Object> find(final String name) {
    return new Result<Element, Object>() {

      public Element in(Object target) {
        if (name.indexOf('.') > -1) {
          return NestedElement.createNestedElement(target, name);
        }
        Finder<Element> finder = getFinder(target);
        return finder.find(name).in(target);
      }

    };
  }

  @Override
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {

      public Set<Element> in(Object target) {
        Finder<Element> finder = getFinder(target);
        return finder.findAll().in(target);
      }

    };
  }

}
