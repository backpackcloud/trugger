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
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Result;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.MethodPredicates.getter;
import static tools.devnull.trugger.reflection.MethodPredicates.setter;
import static tools.devnull.trugger.reflection.Reflection.*;

/**
 * A default class for finding properties in objects.
 *
 * @author Marcelo Guimarães
 */
public final class ObjectElementFinder implements Finder<Element> {

  private final ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      loadUsingFields(type, map);
      loadUsingMethods(type, map);
    }

    private void loadUsingFields(Class type, Map<String, Element> map) {
      List<Field> fields = fields().in(type);
      for (Field field : fields) {
        if (!map.containsKey(field.getName())) {
          ObjectElement prop = new ObjectElement(field);
          map.put(prop.name(), prop);
        }
      }
    }

    private void loadUsingMethods(Class type, Map<String, Element> map) {
      List<Method> declaredMethods = methods()
          .filter(
              getter().or(setter()))
          .in(type);
      for (Method method : declaredMethods) {
        String name = Reflection.parsePropertyName(method);
        if (!map.containsKey(name)) {
          ObjectElement prop = new ObjectElement(method, name);
          map.put(prop.name(), prop);
        }
      }
    }
  };

  public final Result<Element, Object> find(final String propertyName) {
    return target -> {
      for (Class type : hierarchyOf(target)) {
        Element element = cache.get(type, propertyName);
        if (element != null) {
          return target instanceof Class ?
              element : new SpecificElement(element, target);
        }
      }
      return null;
    };
  }

  public Result<List<Element>, Object> findAll() {
    return target -> {
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
    };
  }
}
