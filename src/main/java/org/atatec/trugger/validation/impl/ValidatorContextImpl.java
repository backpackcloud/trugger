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
package org.atatec.trugger.validation.impl;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.ValidatorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ValidatorContextImpl implements ValidatorContext {

  private final Annotation annotation;
  private final Element element;
  private final Object target;
  private final String validationContext;

  public ValidatorContextImpl(Annotation annotation) {
    this(annotation, null, null, null);
  }

  public ValidatorContextImpl(Annotation annotation, Object target) {
    this(annotation, null, target, null);
  }

  public ValidatorContextImpl(Annotation annotation, Element element, Object target) {
    this(annotation, element, target, null);
  }

  public ValidatorContextImpl(Annotation annotation, Element element, Object target, String validationContext) {
    this.annotation = annotation;
    this.element = element;
    this.target = target;
    this.validationContext = validationContext;
  }

  public Annotation annotation() {
    return annotation;
  }

  public Object target() {
    return target;
  }

  public AnnotatedElement annotatedElement() {
    return element;
  }

  public Element element() {
    return element;
  }

  public String validationContext() {
    return validationContext;
  }

}
