/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.property.impl;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.ElementFinderHelper;
import net.sf.trugger.reflection.ClassHierarchyFinder;
import net.sf.trugger.reflection.ClassHierarchyIteration;
import org.apache.commons.collections.map.LRUMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static net.sf.trugger.reflection.Reflection.*;
import static net.sf.trugger.reflection.ReflectionPredicates.*;

/**
 * A default class for finding properties in objects.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public final class ObjectPropertyFinder implements Finder<Element> {
  
  private final Map<Class<?>, Map<String, Element>> cache;
  
  public ObjectPropertyFinder() {
    cache = Collections.synchronizedMap(new LRUMap(300));
  }
  
  public final Result<Element, Object> find(final String propertyName) {
    return new Result<Element, Object>() {
      
      public Element in(Object target) {
        return new ClassHierarchyFinder<Element>() {
          
          @Override
          protected Element findObject(Class<?> clazz) {
            Map<String, Element> map = getFromCache(clazz);
            if (map.containsKey(propertyName)) {
              return map.get(propertyName);
            }
            return NO_FIND;
          }
        }.find(target);
      }
    };
  }
  
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {
      
      public Set<Element> in(Object target) {
        final Map<String, Element> map = new HashMap<String, Element>();
        new ClassHierarchyIteration() {
          
          @Override
          protected void iteration(Class<?> clazz) {
            Set<Element> properties = _getProperties(clazz);
            for (Element property : properties) {
              String name = property.name();
              //used in case of a property override
              if (!map.containsKey(name)) {
                map.put(name, property);
              }
            }
          }
        }.iterate(target);
        Collection<Element> elements = map.values();
        return ElementFinderHelper.computeResult(target, elements);
      }
    };
  }
  
  /**
   * @param objectClass
   *          the object class
   * @return a list with all the object properties declared in its class.
   */
  private Set<Element> _getProperties(Class<?> objectClass) {
    return new HashSet<Element>(getFromCache(objectClass).values());
  }
  
  private Map<String, Element> getFromCache(Class<?> type) {
    Map<String, Element> map = cache.get(type);
    if (map == null) {
      map = new HashMap<String, Element>(20);
      Set<Method> declaredMethods = methods().nonStatic()
        .thatMatches(GETTER.or(SETTER))
      .in(type);
      for (Method method : declaredMethods) {
        String name = resolvePropertyName(method);
        if(!map.containsKey(name)) {
          ObjectProperty prop = new ObjectProperty(method, name);
          map.put(prop.name(), prop);
        }
      }
      Set<Field> fields = fields().nonStatic()
        .thatMatches(PUBLIC.negate())
      .in(type);
      for (Field field : fields) {
        if(!map.containsKey(field.getName())) {
          Method getter = reflect().getterFor(field).in(type);
          Method setter = reflect().setterFor(field).in(type);
          if((getter != null) || (setter != null)) {
            ObjectProperty prop = new ObjectProperty(field, getter, setter);
            map.put(prop.name(), prop);
          }
        }
      }
      cache.put(type, map);
    }
    return map;
  }
  
  /**
   * Compute and returns the property name encapsulated by the given method. The
   * given method must be a getter or a setter.
   */
  private static String resolvePropertyName(Method method) {
    String name = method.getName();
    int i = name.startsWith("is") ? 2 : 3;
    return Character.toLowerCase(name.charAt(i++)) + name.substring(i);
  }
  
}
