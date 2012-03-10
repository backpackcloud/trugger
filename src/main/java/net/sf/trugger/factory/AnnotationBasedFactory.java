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
package net.sf.trugger.factory;

import net.sf.trugger.CreateException;
import net.sf.trugger.annotation.DomainAnnotatedElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * A factory that uses an Annotation to identify the instance type for creating.
 * The Annotation and the element name that indicates the class can be
 * configured and the default name is <i>value</i>.
 * <p>
 * This class also supports nested annotations by scanning the annotations of
 * the key.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <A>
 *          the annotation type
 * @param <E>
 *          the object type
 */
public class AnnotationBasedFactory<A extends Annotation, E> extends BaseFactory<AnnotatedElement, E> implements
    Factory<AnnotatedElement, E> {

  /**
   * The annotation type.
   */
  protected final Class<? extends Annotation> annotationType;
  /**
   * The property to extract the class for instantiation.
   */
  private final String elementName;

  private ThreadLocal<DomainAnnotatedElement> domainAnnotatedElement = new ThreadLocal<DomainAnnotatedElement>();

  /**
   * Creates a new AnnotationBasedFactory based on the specified arguments.
   *
   * @param annotationType
   *          the annotation type that contains the object class
   */
  public AnnotationBasedFactory(Class<A> annotationType) {
    this(annotationType, "value");
  }

  /**
   * Creates a new AnnotationBasedFactory based on the specified arguments.
   *
   * @param annotationType
   *          the annotation type that contains the object class
   * @param elementName
   *          the element name to get the class.
   */
  public AnnotationBasedFactory(Class<A> annotationType, String elementName) {
    this.annotationType = annotationType;
    this.elementName = elementName;
  }

  /**
   * Creates a new AnnotationBasedFactory.
   * <p>
   * The use of this constructor indicates that the generic parameter that
   * defines the annotation type is defined.
   */
  protected AnnotationBasedFactory() {
    this("value");
  }

  /**
   * Creates a new AnnotationBasedFactory based on the specified arguments.
   * <p>
   * The use of this constructor indicates that the generic parameter that
   * defines the annotation type is defined.
   *
   * @param elementName
   *          the element name to get the class.
   */
  protected AnnotationBasedFactory(String elementName) {
    this.annotationType = Reflection.reflect().genericType("A").in(this);
    this.elementName = elementName;
  }

  public boolean canCreate(AnnotatedElement key) {
    initialize(key);
    return domainAnnotatedElement().isAnnotationPresent(annotationType);
  }

  private void initialize(AnnotatedElement key) {
    DomainAnnotatedElement element = domainAnnotatedElement.get();
    if(element == null || !element.annotatedElement().equals(key)) {
      domainAnnotatedElement.set(DomainAnnotatedElement.wrap(key));
    }
  }

  public E create(AnnotatedElement key) throws CreateException {
    try {
      initialize(key);
      return super.create(key);
    } finally {
      domainAnnotatedElement.remove();
    }
  }

  /**
   * @return the wrapped DomainAnnotatedElement for the key
   */
  protected final DomainAnnotatedElement domainAnnotatedElement() {
    return domainAnnotatedElement.get();
  }

  @Override
  protected final Class<? extends E> resolveClassForCreation(AnnotatedElement key) {
    if (!key.isAnnotationPresent(annotationType)) {
      key = domainAnnotatedElement();
    }
    Annotation classIdentifier = key.getAnnotation(annotationType);
    Element element = Elements.element(elementName).in(annotationType);
    if (element == null) {
      throw new CreateException("Cannot find property " + elementName + " in " + annotationType.getName());
    }
    return element.in(classIdentifier).value();
  }

}
