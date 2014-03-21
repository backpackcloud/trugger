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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.methods;

/**
 * A default class for finding properties in annotations.
 * <p/>
 * All methods declared on the annotation will be treat as a property.
 *
 * @author Marcelo Guimarães
 */
public final class AnnotationElementFinder implements Finder<Element> {

  private ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      Set<Method> declaredMethods = methods().in(type);
      AnnotationElement prop;
      for (Method method : declaredMethods) {
        prop = new AnnotationElement(method);
        map.put(prop.name(), prop);
      }
    }
  };

  public Result<Set<Element>, Object> findAll() {
    return target -> {
      Collection<Element> elements = cache.get(target);
      return ElementFinderHelper.computeResult(target, elements);
    };
  }

  public final Result<Element, Object> find(final String propertyName) {
    return target -> {
      Element property = cache.get(target, propertyName);
      if (target instanceof Class<?>) {
        return property;
      } else if (property != null) {
        return new SpecificElement(property, target);
      }
      return null;
    };
  }

}
