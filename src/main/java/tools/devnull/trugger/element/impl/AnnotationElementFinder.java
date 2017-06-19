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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * A default class for finding properties in annotations.
 * <p>
 * All methods declared on the annotation will be treat as a property.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public final class AnnotationElementFinder implements ElementFinder {

  private ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      List<Method> declaredMethods = reflect().methods().from(type);
      AnnotationElement prop;
      for (Method method : declaredMethods) {
        prop = new AnnotationElement(method);
        map.put(prop.name(), prop);
      }
    }
  };

  public List<Element> findAll(Object target) {
    Collection<Element> elements = cache.get(target);
    if (target instanceof Class<?>) {
      return new ArrayList<>(elements);
    }
    return elements.stream().map(
        element -> new SpecificElement(element, target)
    ).collect(Collectors.toList());
  }

  @Override
  public final Optional<Element> find(String propertyName, Object target) {
    Element property = cache.get(target, propertyName);
    if (target instanceof Class<?>) {
      return Optional.of(property);
    } else if (property != null) {
      return Optional.of(new SpecificElement(property, target));
    }
    return Optional.empty();
  }

}
