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
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;

import java.lang.annotation.Annotation;

/**
 * Base class for all decorated elements.
 *
 * @author Marcelo Guimarães
 */
public class DecoratedElement implements Element {

  protected final Element element;

  public DecoratedElement(Element decorated) {
    this.element = decorated;
  }

  public Class declaringClass() {
    return element.declaringClass();
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

  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return element.isAnnotationPresent(annotationClass);
  }

  public boolean isSpecific() {
    return element.isSpecific();
  }

  public <E> E target() {
    return (E) element.target();
  }

  public String name() {
    return element.name();
  }

  public Class type() {
    return element.type();
  }

  @Override
  public void value(Object value) throws HandlingException {
    in(target()).value(value);
  }

  @Override
  public <E> E value() throws HandlingException {
    return (E) in(target()).value();
  }

  @Override
  public ValueHandler in(Object target) {
    return element.in(target);
  }

  public boolean isReadable() {
    return element.isReadable();
  }

  public boolean isWritable() {
    return element.isWritable();
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
