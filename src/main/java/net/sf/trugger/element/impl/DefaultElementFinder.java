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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.property.Properties;
import net.sf.trugger.reflection.Reflection;

/**
 * A default finder for every type of objects. It uses the properties and fields
 * for resolving the elements.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class DefaultElementFinder implements Finder<Element> {

  @Override
  public Result<Element, Object> find(final String name) {
    return new Result<Element, Object>() {

      public Element in(Object target) {
        Element propertyElement = null;
        Element fieldElement = null;
        boolean specific = !(target instanceof Class<?>);
        Element property = Properties.property(name).in(target);
        if (property != null) {
          propertyElement = specific ? new SpecificElement(property, target) : property;
        }
        Field field = null;
        if (property == null) {
          field = Reflection.reflect().field(name).recursively().in(target);
          if (field != null) {
            fieldElement = specific ? new SpecificElement(new FieldElement(field), target) : new FieldElement(field);
          }
        }
        return propertyElement != null ? propertyElement : fieldElement;
      }

    };
  }

  @Override
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {

      public Set<Element> in(Object target) {
        Map<String, Element> map = new HashMap<String, Element>();
        boolean specific = !(target instanceof Class);
        Set<Element> properties = Properties.properties().in(target);
        Set<Field> fields = Reflection.reflect().fields().recursively().in(target);

        for (Element property : properties) {
          Element element = specific ? new SpecificElement(property, target) : property;
          map.put(property.name(), element);
        }
        for (Field field : fields) {
          if (!map.containsKey(field.getName())) {
            Element element = specific ? new SpecificElement(new FieldElement(field), target) : new FieldElement(field);
            map.put(field.getName(), element);
          }
        }
        return new HashSet<Element>(map.values());
      }

    };
  }

}
