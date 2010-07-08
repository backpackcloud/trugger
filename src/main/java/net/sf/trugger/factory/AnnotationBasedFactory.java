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

import net.sf.trugger.CreateException;
import net.sf.trugger.bind.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.reflection.Reflection;

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

  private ThreadLocal<Annotation> parent = new ThreadLocal<Annotation>();

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
    return key.isAnnotationPresent(annotationType) || search(key) != null;
  }

  public E create(AnnotatedElement key) throws CreateException {
    try {
      return super.create(key);
    } finally {
      parent.remove();
    }
  }

  /**
   * Searches in the key annotations for an annotation that has the
   * {@link #annotationType defined annotation}.
   *
   * @param key
   *          the key passed to create the object.
   * @return the found element
   */
  protected Annotation search(AnnotatedElement key) {
    Annotation found = null;
    Annotation[] annotations = key.getAnnotations();
    for (Annotation annotation : annotations) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (type.isAnnotationPresent(annotationType)) {
        found = annotation;
        break;
      } else if (!type.getPackage().getName().equals("java.lang.annotation")) {
        Annotation deeper = search(type);
        if (deeper != null) {
          found = deeper;
          break;
        }
      }
    }
    parent.set(found);
    return found;
  }

  /**
   * Returns the annotation that has the desired annotation type or
   * <code>null</code> if the key itself has the annotation type present.
   * <p>
   * This method is only avaiable after
   *
   * @return the found annotation
   */
  protected final Annotation parentAnnotation() {
    return parent.get();
  }

  @Override
  protected final Class<? extends E> resolveClassForCreation(AnnotatedElement key) {
    if (!key.isAnnotationPresent(annotationType)) {
      key = search(key).annotationType();
    }
    Annotation classIdentifier = key.getAnnotation(annotationType);
    Element element = Elements.element(elementName).in(annotationType);
    if (element == null) {
      throw new CreateException("Cannot find property " + elementName + " in " + annotationType.getName());
    }
    return element.in(classIdentifier).value();
  }

  /**
   * Binds the annotation that contains the annotation used to create an
   * instance.
   *
   * @param key
   *          the key passed to this factory.
   * @param instance
   *          the instance to bind the annotation.
   * @return the given instance
   * @since 2.7
   */
  protected E bindAnnotation(AnnotatedElement key, E instance) {
    if (!key.isAnnotationPresent(annotationType)) {
      Binder binder = Bind.newBind();
      registerAnnotation(binder, key);
      binder.applyBinds(instance);
    }
    return instance;
  }

  /**
   * Registers the annotation that contains the annotation used to create an
   * instance in the given binder.
   *
   * @param binder
   *          the binder for registering the bind.
   * @param key
   *          the key passed to this factory.
   * @since 2.7
   */
  protected void registerAnnotation(Binder binder, AnnotatedElement key) {
    if (!key.isAnnotationPresent(annotationType)) {
      Annotation annotation = parentAnnotation();
      binder.bind(annotation).toElement().ofType(annotation.annotationType());
    }
  }

}
