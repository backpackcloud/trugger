/*
 * Copyright 2009-2012 Marcelo Guimarães
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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.NonSpecificElementException;
import org.atatec.trugger.util.Null;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * A common abstraction for an Element.
 *
 * @author Marcelo Guimarães
 */
public abstract class AbstractElement implements Element {

  /**
   * The element for retrieving annotation metadata.
   */
  protected AnnotatedElement annotatedElement = Null.NULL_ANNOTATED_ELEMENT;
  /**
   * The element name
   */
  protected final String name;

  /**
   * @param name
   *          the element name
   */
  public AbstractElement(String name) {
    this.name = name;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public Class<?> type() {
    return Object.class;
  }

  @Override
  public final <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return annotatedElement.getAnnotation(annotationClass);
  }

  @Override
  public final Annotation[] getAnnotations() {
    return annotatedElement.getAnnotations();
  }

  @Override
  public final Annotation[] getDeclaredAnnotations() {
    return annotatedElement.getDeclaredAnnotations();
  }

  @Override
  public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return annotatedElement.isAnnotationPresent(annotationClass);
  }

  @Override
  public boolean isSpecific() {
    return false;
  }

  @Override
  public <E> E target() {
    return null;
  }

  @Override
  public void value(Object value) throws HandlingException {
    if (isSpecific()) {
      in(target()).value(value);
    } else {
      throw new NonSpecificElementException();
    }
  }

  @Override
  public <E> E value() throws HandlingException {
    if (isSpecific()) {
      return (E) in(target()).value();
    }
    throw new NonSpecificElementException();
  }

  @Override
  public String toString() {
    return name + " : " + type().getName();
  }

}
