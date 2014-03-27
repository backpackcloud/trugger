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
package org.atatec.trugger.property.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.ClassElementsCache;
import org.atatec.trugger.element.impl.ElementFinderHelper;
import org.atatec.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.atatec.trugger.reflection.MethodPredicates.*;
import static org.atatec.trugger.reflection.Reflection.*;
import static org.atatec.trugger.reflection.ReflectionPredicates.declaring;

/**
 * A default class for finding properties in objects.
 *
 * @author Marcelo Guimarães
 */
public final class ObjectPropertyFinder implements Finder<Element> {

  private Method searchMethod(Class type, Predicate<Method> predicate) {
    List<Method> candidates = reflect().methods().recursively()
        .filter(predicate).in(type);
    return candidates.isEmpty() ? null : candidates.iterator().next();
  }

  private final ClassElementsCache cache = new ClassElementsCache() {
    @Override
    protected void loadElements(Class type, Map<String, Element> map) {
      Predicate<Member> nonStatic = declaring(Modifier.STATIC).negate();
      List<Method> declaredMethods = methods()
          .filter(
              (getter().or(setter())).and(nonStatic))
          .in(type);
      for (Method method : declaredMethods) {
        String name = Reflection.parsePropertyName(method);
        if (!map.containsKey(name)) {
          ObjectProperty prop = new ObjectProperty(method, name);
          map.put(prop.name(), prop);
        }
      }
      List<Field> fields = fields().filter(nonStatic).in(type);
      for (Field field : fields) {
        if (!map.containsKey(field.getName())) {
          Method getter = searchMethod(type, getterOf(field));
          Method setter = searchMethod(type, setterOf(field));
          if ((getter != null) || (setter != null)) {
            ObjectProperty prop = new ObjectProperty(field, getter, setter);
            map.put(prop.name(), prop);
          }
        }
      }
    }
  };

  public final Result<Element, Object> find(final String propertyName) {
    return target -> {
      for (Class type : hierarchyOf(target)) {
        Element element = cache.get(type, propertyName);
        if (element != null) {
          return element;
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
      return ElementFinderHelper.computeResult(target, elements);
    };
  }

}
