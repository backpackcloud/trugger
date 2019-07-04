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
import io.backpackcloud.trugger.reflection.ReflectedMethod;
import io.backpackcloud.trugger.reflection.Reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A default class for finding properties in annotations.
 * <p>
 * All methods declared on the annotation will be treat as a property.
 *
 * @author Marcelo Guimaraes
 */
public final class AnnotationElementFinder implements ElementFinder {

  @Override
  public boolean canFind(Class type) {
    return type.isAnnotation();
  }

  private ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      Reflection.reflect()
          .methods()
          .from(type)
          .stream()
          .map(ReflectedMethod::unwrap)
          .map(AnnotationElement::new)
          .forEach(prop -> map.put(prop.name(), prop));
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
      return Optional.ofNullable(property);
    } else if (property != null) {
      return Optional.of(new SpecificElement(property, target));
    }
    return Optional.empty();
  }

}
