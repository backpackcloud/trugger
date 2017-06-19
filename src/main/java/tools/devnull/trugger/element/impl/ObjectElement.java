/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.UnreadableElementException;
import tools.devnull.trugger.element.UnwritableElementException;
import tools.devnull.trugger.reflection.ReflectionException;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.function.Predicate;

import static tools.devnull.trugger.reflection.MethodPredicates.*;
import static tools.devnull.trugger.reflection.Reflection.*;

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
 * @author Marcelo Guimarães
 */
public final class ObjectElement extends AbstractElement {

  private Field field;
  private Method getter;
  private Method setter;
  private Class<?> type;
  private Class<?> declaringClass;

  public ObjectElement(Field field) {
    super(field.getName());
    this.field = field;
    this.type = field.getType();
    this.declaringClass = field.getDeclaringClass();
    searchForGetter();
    searchForSetter();
    this.type = getter != null ? getter.getReturnType() :
        setter != null ? setter.getParameterTypes()[0] : field.getType();
    searchForAnnotatedElement();
  }

  /**
   * Creates a new ObjectElement based on the specified method. Only a getter
   * or a setter.
   */
  public ObjectElement(Method method, String name) {
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
    field = reflect().field(name).from(declaringClass).result();
    searchForAnnotatedElement();
  }

  public ValueHandler on(final Object target) {
    return new ValueHandler() {

      public <E> E get() throws HandlingException {
        if (!isReadable()) {
          throw new UnreadableElementException(name);
        }
        try {
          if (getter != null) {
            return invoke(getter).on(target).withoutArgs();
          } else {
            return handle(field).on(target).get();
          }
        } catch (ReflectionException e) {
          throw new HandlingException(e.getCause());
        }
      }

      public void set(Object value) throws HandlingException {
        if (!isWritable()) {
          throw new UnwritableElementException(name);
        }
        try {
          if (setter != null) {
            invoke(setter).on(target).withArgs(value);
          } else {
            handle(field).on(target).set(value);
          }
        } catch (ReflectionException e) {
          throw new HandlingException(e.getCause());
        }
      }
    };
  }

  public boolean isReadable() {
    return getter != null || field != null;
  }

  public boolean isWritable() {
    return setter != null ||
        (field != null && !Modifier.isFinal(field.getModifiers()));
  }

  public Class<?> declaringClass() {
    return declaringClass;
  }

  @Override
  public Class<?> type() {
    return type;
  }

  private void searchForSetter() {
    setter = searchMethod(setterOf(name).and(withParameters(type)));
  }

  private void searchForGetter() {
    getter = searchMethod(getterOf(name).and(returns(type)));
  }

  private Method searchMethod(Predicate<Method> predicate) {
    Collection<Method> candidates = reflect().methods().deep()
        .filter(predicate).from(declaringClass);
    return candidates.isEmpty() ? null : candidates.iterator().next();
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
