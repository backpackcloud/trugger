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
import org.atatec.trugger.HandlingException;
import org.atatec.trugger.Result;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.property.Properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.reflection.Reflection.fields;

/**
 * A default finder for every type of objects. It uses the properties and fields for
 * resolving the elements.
 *
 * @author Marcelo Guimarães
 */
public class DefaultElementFinder implements Finder<Element> {

  @Override
  public Result<Element, Object> find(final String name) {
    return target -> {
      Element propertyElement = null;
      Element fieldElement = null;
      boolean specific = !(target instanceof Class<?>);
      if (specific && target.getClass().isArray()) {
        return new ArrayElementFinder().find(name).in(target);
      }
      Element property = Properties.property(name).in(target);
      if (property != null) {
        propertyElement = specific ? new SpecificElement(property, target) : property;
      }
      Field field = field(name).deep().in(target);
      if (field != null) {
        fieldElement = specific ? new SpecificElement(new FieldElement(field), target) : new FieldElement(field);
      }
      if (property != null && field != null) {
        return new MergedElement(fieldElement, propertyElement);
      }
      return propertyElement != null ? propertyElement : fieldElement;
    };
  }

  @Override
  public Result<List<Element>, Object> findAll() {
    return target -> {
      Map<String, Element> map = new HashMap<>();
      boolean specific = !(target instanceof Class);
      if (specific && target.getClass().isArray()) {
        return new ArrayElementFinder().findAll().in(target);
      }
      List<Element> properties = Properties.properties().in(target);
      List<Field> fields = fields().deep().in(target);

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
      return new ArrayList<>(map.values());
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

        public void set(Object value) throws HandlingException {
          forWrite.in(target).set(value);
        }

        public <E> E get() throws HandlingException {
          return forRead.in(target).get();
        }
      };
    }

    public <E> E get() throws HandlingException {
      return forRead.get();
    }

    public void set(Object value) throws HandlingException {
      forWrite.set(value);
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
      return decorated.target();
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

  }

}
