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
package net.sf.trugger.element.impl;

import net.sf.trugger.Finder;
import net.sf.trugger.HandlingException;
import net.sf.trugger.Result;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.property.Properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.sf.trugger.reflection.Reflection.field;
import static net.sf.trugger.reflection.Reflection.fields;

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
        Field field = field(name).recursively().in(target);
        if (field != null) {
          fieldElement = specific ? new SpecificElement(new FieldElement(field), target) : new FieldElement(field);
        }
        if(property != null && field != null) {
          return new MergedElement(fieldElement, propertyElement);
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
        Set<Field> fields = fields().recursively().in(target);

        for (Element property : properties) {
          Element element = specific ? new SpecificElement(property, target) : property;
          map.put(property.name(), element);
        }
        for (Field field : fields) {
          Element element = specific ? new SpecificElement(new FieldElement(field), target) : new FieldElement(field);
          if (!map.containsKey(field.getName())) {
            map.put(field.getName(), element);
          } else {
            map.put(field.getName(), new MergedElement(element, map.get(field.getName())));
          }
        }
        return new HashSet<Element>(map.values());
      }

    };
  }

  private static class MergedElement implements Element {

    private final Element forRead;
    private final Element forWrite;
    private final Element decorated;

    private MergedElement(Element field, Element property) {
      this.decorated = property;
      this.forRead = property.isReadable() ? property : field;
      this.forWrite = property.isWritable() ? property : field;
    }

    public boolean isReadable() {
      return forRead.isReadable();
    }

    public boolean isWritable() {
      return forWrite.isWritable();
    }

    public ValueHandler in(final Object target) {
      return new ValueHandler() {

        public void value(Object value) throws HandlingException {
          forWrite.in(target).value(value);
        }

        public <E> E value() throws HandlingException {
          return (E) forRead.in(target).value();
        }
      };
    }

    public <E> E value() throws HandlingException {
      return (E) forRead.value();
    }

    public void value(Object value) throws HandlingException {
      forWrite.value(value);
    }

    public Class declaringClass() {
      return decorated.declaringClass();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return decorated.getAnnotation(annotationClass);
    }

    public Annotation[] getAnnotations() {
      return decorated.getAnnotations();
    }

    public Annotation[] getDeclaredAnnotations() {
      return decorated.getDeclaredAnnotations();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
      return decorated.isAnnotationPresent(annotationClass);
    }

    public boolean isSpecific() {
      return decorated.isSpecific();
    }

    public <E> E target() {
      return (E) decorated.target();
    }

    public String name() {
      return decorated.name();
    }

    public Class type() {
      return decorated.type();
    }

    public String toString() {
      return decorated.toString();
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if(obj instanceof DefaultElementFinder.MergedElement) {
        return ((DefaultElementFinder.MergedElement) obj).decorated.equals(decorated);
      }
      return decorated.equals(obj);
    }

    public int hashCode() {
      return decorated.hashCode();
    }

  }

}
