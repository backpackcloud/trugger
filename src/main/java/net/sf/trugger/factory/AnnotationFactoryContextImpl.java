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
package net.sf.trugger.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import net.sf.trugger.element.Element;
import net.sf.trugger.util.Utils;

/**
 * A default implementation for {@link AnnotationFactoryContext}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class AnnotationFactoryContextImpl implements AnnotationFactoryContext {

  protected AnnotatedElement annotatedElement;

  protected Object target;

  protected Annotation annotation;

  public AnnotationFactoryContextImpl() {

  }

  public AnnotationFactoryContextImpl(Annotation annotation, AnnotatedElement annotatedElement, Object target) {
    this.annotation = annotation;
    this.annotatedElement = annotatedElement;
    this.target = target;
  }

  public AnnotationFactoryContextImpl(Annotation annotation, AnnotatedElement annotatedElement) {
    this.annotation = annotation;
    this.annotatedElement = annotatedElement;
  }

  public AnnotationFactoryContextImpl(Annotation annotation, Object target) {
    this.annotation = annotation;
    this.target = target;
  }

  public AnnotationFactoryContextImpl(Annotation annotation) {
    this.annotation = annotation;
  }

  public AnnotationFactoryContextImpl(AnnotatedElement annotatedElement) {
    this.annotatedElement = annotatedElement;
  }

  public AnnotationFactoryContextImpl(AnnotatedElement annotatedElement, Object target) {
    this.annotatedElement = annotatedElement;
    this.target = target;
  }

  /* (non-Javadoc)
   * @see net.sf.trugger.factory.AnnotationFactoryContext#annotatedElement()
   */
  @Override
  public AnnotatedElement annotatedElement() {
    return annotatedElement == null ? Utils.resolveType(annotation) : annotatedElement;
  }

  /* (non-Javadoc)
   * @see net.sf.trugger.factory.AnnotationFactoryContext#annotation()
   */
  @Override
  public Annotation annotation() {
    return annotation;
  }

  /* (non-Javadoc)
   * @see net.sf.trugger.factory.AnnotationFactoryContext#element()
   */
  @Override
  public Element element() {
    return (Element) (annotatedElement instanceof Element ? annotatedElement : null);
  }

  /* (non-Javadoc)
   * @see net.sf.trugger.factory.AnnotationFactoryContext#target()
   */
  @Override
  public Object target() {
    return target;
  }

  public void setAnnotatedElement(AnnotatedElement annotatedElement) {
    this.annotatedElement = annotatedElement;
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  public void setAnnotation(Annotation annotation) {
    this.annotation = annotation;
  }

}
