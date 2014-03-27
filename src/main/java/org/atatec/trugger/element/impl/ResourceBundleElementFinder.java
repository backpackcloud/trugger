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

import java.util.*;

/**
 * A finder for ResourceBundle elements.
 *
 * @author Marcelo Guimarães
 */
public class ResourceBundleElementFinder implements Finder<Element> {
  
  @Override
  public Result<List<Element>, Object> findAll() {
    return target -> {
      if (target instanceof Class<?>) {
        return Collections.emptyList();
      }
      List<Element> properties = new ArrayList<>();
      ResourceBundle bundle = (ResourceBundle) target;
      for (String key : bundle.keySet()) {
        properties.add(new SpecificElement(new ResourceBundleElement(key), bundle));
      }
      return properties;
    };
  }
  
  @Override
  public Result<Element, Object> find(final String name) {
    return target -> {
      if (target instanceof Class<?>) {
        return new ResourceBundleElement(name);
      }
      return new SpecificElement(new ResourceBundleElement(name), target);
    };
  }
  
}
