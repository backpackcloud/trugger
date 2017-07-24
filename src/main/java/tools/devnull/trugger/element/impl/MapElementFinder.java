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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class MapElementFinder implements ElementFinder {

  @Override
  public boolean canFind(Class type) {
    return Map.class.isAssignableFrom(type) && !Properties.class.isAssignableFrom(type);
  }

  @Override
  public List<Element> findAll(Object target) {
    if (target instanceof Class<?>) {
      return Collections.emptyList();
    }
    List<Element> properties = new ArrayList<>();
    Map map = (Map) target;
    for (Object key : map.keySet()) {
      if (key instanceof String) {
        properties.add(new SpecificElement(new MapElement((String) key), map));
      }
    }
    return properties;
  }

  @Override
  public Optional<Element> find(String name, Object target) {
    if (target instanceof Class<?>) {
      return Optional.of(new MapElement(name));
    }
    return Optional.of(new SpecificElement(new MapElement(name), target));
  }

}
