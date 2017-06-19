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
import tools.devnull.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.MethodPredicates.getter;
import static tools.devnull.trugger.reflection.MethodPredicates.setter;
import static tools.devnull.trugger.reflection.Reflection.hierarchyOf;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * A default class for finding properties in objects.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public final class ObjectElementFinder implements ElementFinder {

  private final ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      loadUsingFields(type, map);
      loadUsingMethods(type, map);
    }

    private void loadUsingFields(Class type, Map<String, Element> map) {
      List<Field> fields = reflect().fields().from(type);
      for (Field field : fields) {
        if (!map.containsKey(field.getName())) {
          ObjectElement prop = new ObjectElement(field);
          map.put(prop.name(), prop);
        }
      }
    }

    private void loadUsingMethods(Class type, Map<String, Element> map) {
      List<Method> declaredMethods = reflect().methods()
          .filter(
              getter().or(setter()))
          .from(type);
      for (Method method : declaredMethods) {
        String name = Reflection.parsePropertyName(method);
        if (!map.containsKey(name)) {
          ObjectElement prop = new ObjectElement(method, name);
          map.put(prop.name(), prop);
        }
      }
    }
  };

  public final Optional<Element> find(String propertyName, Object target) {
    for (Class type : hierarchyOf(target)) {
      Element element = cache.get(type, propertyName);
      if (element != null) {
        return Optional.of(target instanceof Class ? element : new SpecificElement(element, target));
      }
    }
    return Optional.empty();
  }

  public List<Element> findAll(Object target) {
    final Map<String, Element> map = new HashMap<String, Element>();
    for (Class type : hierarchyOf(target)) {
      Collection<Element> properties = cache.get(type);
      for (Element property : properties) {
        String name = property.name();
        //used in case of a property override
        if (!map.containsKey(name)) {
          map.put(name, property);
        }
      }
    }
    Collection<Element> elements = map.values();
    if (target instanceof Class<?>) {
      return new ArrayList<>(elements);
    }
    return elements.stream().map(
        element -> new SpecificElement(element, target)
    ).collect(Collectors.toList());
  }
}
