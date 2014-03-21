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

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Marcelo Guimarães
 */
public class PropertiesElementFinder implements Finder<Element> {
  
  @Override
  public Result<Set<Element>, Object> findAll() {
    return target -> {
      if (target instanceof Class<?>) {
        return Collections.emptySet();
      }
      Set<Element> elements = new HashSet<Element>();
      Properties props = (Properties) target;
      for (Object key : props.keySet()) {
        if (key instanceof String) {
          elements.add(new SpecificElement(new PropertiesElement((String) key), props));
        }
      }
      return elements;
    };
  }
  
  @Override
  public Result<Element, Object> find(final String name) {
    return target -> {
      if (target instanceof Class<?>) {
        return new PropertiesElement(name);
      }
      return new SpecificElement(new PropertiesElement(name), target);
    };
  }
  
}
