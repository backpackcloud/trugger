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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.UnreadableElementException;
import org.atatec.trugger.element.UnwritableElementException;
import org.atatec.trugger.element.impl.AbstractElement;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.util.HashBuilder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.returns;

/**
 * This class represents an object property.
 * <p>
 * The property can be defined by fields, getters or setters methods. If more
 * than one are present, only one will be used to return annotations. Here is
 * the list in priority order:
 * <ol>
 * <li>getter method</li>
 * <li>field</li>
 * <li>setter method</li>
 * </ol>
 * <p>
 * For value manipulations (write and read), the methods are used all the time.
 *
 * @author Marcelo Varella Barca Guimarães
 */
final class ObjectProperty extends AbstractElement {

  private Field field;
  private Method getter;
  private Method setter;
  private Class<?> type;
  private Class<?> declaringClass;
  private boolean readable;
  private boolean writable;

  /**
   * Creates a new ObjectProperty based on the specified method. Only a getter
   * or a setter.
   */
  ObjectProperty(Method method, String name) {
    super(name);
    declaringClass = method.getDeclaringClass();
    boolean isGetter = method.getParameterTypes().length == 0;
    if (isGetter) {
      getter = method;
      type = getter.getReturnType();
      searchForSetter();
    } else {
      setter = method;
      type = setter.getParameterTypes()[0];
      searchForGetter();
    }
    field = reflect().field(name).in(declaringClass);
    searchForAnnotatedElement();
    initialize();
  }

  ObjectProperty(Field field, Method getter, Method setter) {
    super(field.getName());
    this.field = field;
    this.getter = getter;
    this.setter = setter;
    declaringClass = field.getDeclaringClass();
    type = getter != null ? getter.getReturnType() : setter != null ? setter.getReturnType() : field.getType();
    searchForAnnotatedElement();
    initialize();
  }

  private void initialize() {
    readable = getter != null;
    writable = setter != null;
  }

  public ValueHandler in(final Object target) {
    return new ValueHandler(){

      public <E> E value() throws HandlingException {
      if (!isReadable()) {
        throw new UnreadableElementException(name);
      }
      try {
        return (E) invoke(getter).in(target).withoutArgs();
      } catch (ReflectionException e) {
        throw new HandlingException(e.getCause());
      }
    }

    public void value(Object value) throws HandlingException {
      if (!isWritable()) {
        throw new UnwritableElementException(name);
      }
      try {
        invoke(setter).in(target).withArgs(value);
      } catch (ReflectionException e) {
        throw new HandlingException(e.getCause());
      }
    }
    };
  }

  public boolean isReadable() {
    return readable;
  }

  public boolean isWritable() {
    return writable;
  }

  public Class<?> declaringClass() {
    return declaringClass;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> type() {
    return type;
  }

  @Override
  public int hashCode() {
    return new HashBuilder(declaringClass).add(name()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ObjectProperty other = (ObjectProperty) obj;
    return declaringClass.equals(other.declaringClass) && name.equals(other.name);
  }

  private void searchForSetter() {
    setter = reflect().setterFor(name).recursively().forType(type).in(declaringClass);
  }

  private void searchForGetter() {
    getter = reflect().getterFor(name).that(returns(type)).recursively().in(declaringClass);
  }

  private void searchForAnnotatedElement() {
    if (isAnyAnnotationPresent(getter)) {
      annotatedElement = getter;
    } else if (isAnyAnnotationPresent(field)) {
      annotatedElement = field;
    } else if (isAnyAnnotationPresent(setter)) {
      annotatedElement = setter;
    }
  }

  private static boolean isAnyAnnotationPresent(AnnotatedElement element) {
    return (element != null) && (element.getAnnotations().length > 0);
  }

}
