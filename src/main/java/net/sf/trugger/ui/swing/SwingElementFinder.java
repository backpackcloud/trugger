/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.ui.swing;

import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.DefaultElementFinder;
import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.registry.MapRegistry;
import net.sf.trugger.registry.Registry;
import net.sf.trugger.registry.Registry.Entry;
import net.sf.trugger.scan.ClassScan;
import net.sf.trugger.transformer.Transformer;
import net.sf.trugger.ui.swing.element.SwingComponentElement;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class SwingElementFinder extends DefaultElementFinder implements Finder<Element> {

  private Registry<Class, Class<? extends Element>> registry;

  private Transformer<Element, Element> transformer = new Transformer<Element, Element>() {

    @Override
    public Element transform(Element element) {
      Class componentElementType = getElementClass(element.type());
      if (componentElementType == null) {
        return element;
      }
      return (Element) Reflection.newInstanceOf(componentElementType, element);
    }
  };

  public SwingElementFinder() {
    registry = new MapRegistry<Class, Class<? extends Element>>();
    Set<Class<?>> classes =
        ClassScan.findClasses().recursively().assignableTo(SwingComponentElement.class)
          .nonAnonymous().thatMatches(ReflectionPredicates.NON_ABSTRACT_CLASS).withAccess(Access.PUBLIC)
          .in(SwingComponentElement.class.getPackage().getName());
    for (Class elementClass : classes) {
      Class type = Reflection.reflect().genericType("T").in(elementClass);
      registry.register(elementClass).to(type);
    }
  }

  private Class getElementClass(Class type) {
    if (registry.hasRegistryFor(type)) {
      return registry.registryFor(type);
    }
    Class<?> superclass = type.getSuperclass();
    // trying to avoid the loop below
    if ((superclass != null) && registry.hasRegistryFor(superclass)) {
      return registry.registryFor(superclass);
    }
    for (Entry<Class, Class<? extends Element>> entry : registry.entries()) {
      if (entry.key().isAssignableFrom(type)) {
        Class<? extends Element> finder = entry.registry();
        registry.register(finder).to(type);
        return finder;
      }
    }
    return null;
  }

  @Override
  public Result<Element, Object> find(String name) {
    final Result<Element, Object> result = super.find(name);
    return new Result<Element, Object>() {

      public Element in(Object target) {
        Element element = result.in(target);
        return transformer.transform(element);
      }
    };
  }

  @Override
  public Result<Set<Element>, Object> findAll() {
    final Result<Set<Element>, Object> result = super.findAll();
    return new Result<Set<Element>, Object>() {

      @Override
      public Set<Element> in(Object target) {
        Set<Element> elements = result.in(target);
        Set<Element> transformed = new HashSet<Element>(elements.size());
        Iteration.copyTo(transformed).transformingWith(transformer).allElements().from(elements);
        return transformed;
      }
    };
  }

}
