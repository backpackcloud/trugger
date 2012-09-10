/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.property.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.ClassElementsCache;
import org.atatec.trugger.element.impl.ElementFinderHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.fields;
import static org.atatec.trugger.reflection.Reflection.hierarchyOf;
import static org.atatec.trugger.reflection.Reflection.methods;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.GETTER;
import static org.atatec.trugger.reflection.ReflectionPredicates.SETTER;

/**
 * A default class for finding properties in objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class ObjectPropertyFinder implements Finder<Element> {

  private final ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      Set<Method> declaredMethods = methods().nonStatic()
        .that(GETTER.or(SETTER))
        .in(type);
      for (Method method : declaredMethods) {
        String name = resolvePropertyName(method);
        if (!map.containsKey(name)) {
          ObjectProperty prop = new ObjectProperty(method, name);
          map.put(prop.name(), prop);
        }
      }
      Set<Field> fields = fields().nonStatic().in(type);
      for (Field field : fields) {
        if (!map.containsKey(field.getName())) {
          Method getter = reflect().getterFor(field).in(type);
          Method setter = reflect().setterFor(field).in(type);
          if ((getter != null) || (setter != null)) {
            ObjectProperty prop = new ObjectProperty(field, getter, setter);
            map.put(prop.name(), prop);
          }
        }
      }
    }
  };

  public final Result<Element, Object> find(final String propertyName) {
    return new Result<Element, Object>() {

      public Element in(Object target) {
        for (Class type : hierarchyOf(target)) {
          Element element = cache.get(type, propertyName);
          if(element != null) {
            return element;
          }
        }
        return null;
      }
    };
  }

  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {

      public Set<Element> in(Object target) {
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
        return ElementFinderHelper.computeResult(target, elements);
      }
    };
  }

  /**
   * Compute and returns the property name encapsulated by the given method. The given
   * method must be a getter or a setter.
   */
  private static String resolvePropertyName(Method method) {
    String name = method.getName();
    int i = name.startsWith("is") ? 2 : 3;
    return Character.toLowerCase(name.charAt(i++)) + name.substring(i);
  }

}
