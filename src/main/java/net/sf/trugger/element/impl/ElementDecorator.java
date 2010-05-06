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

import java.lang.annotation.Annotation;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;

/**
 * A class to decorate Element objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class ElementDecorator implements Element {

  /**
   * The decorated element.
   */
  protected final Element element;

  /**
   * @param element
   *          the decorated element.
   */
  public ElementDecorator(Element element) {
    this.element = element;
  }

  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return element.getAnnotation(annotationClass);
  }

  public Annotation[] getAnnotations() {
    return element.getAnnotations();
  }

  public Annotation[] getDeclaredAnnotations() {
    return element.getDeclaredAnnotations();
  }

  public Class<?> declaringClass() {
    return element.declaringClass();
  }

  public String name() {
    return element.name();
  }

  public Class<?> type() {
    return element.type();
  }

  public ValueHandler in(Object target) {
    return element.in(target);
  }

  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return element.isAnnotationPresent(annotationClass);
  }

  public boolean isReadable() {
    return element.isReadable();
  }

  public boolean isSpecific() {
    return element.isSpecific();
  }

  public boolean isWritable() {
    return element.isWritable();
  }

  public <E> E value() throws HandlingException {
    return (E) element.value();
  }

  public void value(Object value) throws HandlingException {
    element.value(value);
  }

  @Override
  public boolean equals(Object obj) {
    return element.equals(obj);
  }

  @Override
  public int hashCode() {
    return element.hashCode();
  }

  @Override
  public String toString() {
    return element.toString();
  }

}
