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
import java.util.Properties;

/**
 * @author Marcelo Guimarães
 */
public class PropertiesElementFinder implements ElementFinder {

  @Override
  public List<Element> findAll(Object target) {
    if (target instanceof Class<?>) {
      return Collections.emptyList();
    }
    List<Element> elements = new ArrayList<>();
    Properties props = (Properties) target;
    for (Object key : props.keySet()) {
      if (key instanceof String) {
        elements.add(new SpecificElement(new PropertiesElement((String) key), props));
      }
    }
    return elements;
  }

  @Override
  public Optional<Element> find(String name, Object target) {
    if (target instanceof Class<?>) {
      return Optional.of(new PropertiesElement(name));
    }
    return Optional.of(new SpecificElement(new PropertiesElement(name), target));
  }

}
