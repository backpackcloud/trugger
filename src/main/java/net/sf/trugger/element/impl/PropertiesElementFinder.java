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
package net.sf.trugger.element.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertiesElementFinder implements Finder<Element> {
  
  @Override
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {
      
      @Override
      public Set<Element> in(Object target) {
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
      }
    };
  }
  
  @Override
  public Result<Element, Object> find(final String name) {
    return new Result<Element, Object>() {
      
      @Override
      public Element in(Object target) {
        if (target instanceof Class<?>) {
          return new PropertiesElement(name);
        }
        return new SpecificElement(new PropertiesElement(name), target);
      }
    };
  }
  
}
