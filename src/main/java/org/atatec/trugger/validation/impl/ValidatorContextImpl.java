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
package org.atatec.trugger.validation.impl;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.ValidatorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/** @author Marcelo Guimarães */
public class ValidatorContextImpl implements ValidatorContext {

  private Annotation annotation;
  private Element element;
  private Object target;
  private String context;

  public ValidatorContextImpl() {
  }

  public ValidatorContextImpl(Annotation annotation) {
    this.annotation = annotation;
  }

  public ValidatorContextImpl annotation(Annotation annotation) {
    this.annotation = annotation;
    return this;
  }

  public ValidatorContextImpl element(Element element) {
    this.element = element;
    return this;
  }

  public ValidatorContextImpl target(Object target) {
    this.target = target;
    return this;
  }

  public ValidatorContextImpl context(String validationContext) {
    this.context = validationContext;
    return this;
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

  public String context() {
    return context;
  }

}
