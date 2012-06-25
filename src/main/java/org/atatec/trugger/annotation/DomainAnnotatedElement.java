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
package org.atatec.trugger.annotation;

import org.atatec.trugger.annotation.impl.DomainAnnotationImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that defines an {@link AnnotatedElement} that can search in its
 * Annotation hierarchy.
 * <p>
 * This is usefull to create domain annotations. Supose you have a set of
 * annotations that represents something if they are together in the same
 * annotated element. You can put it all in a new annotation that refers to the
 * meaning of the annotations and, if the annotated element is
 * {@link #wrap(AnnotatedElement) wrapped} by this class, the methods
 * {@link #isAnnotationPresent(Class)} and {@link #getAnnotation(Class)} will
 * return that annotations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class DomainAnnotatedElement implements AnnotatedElement {

  private final AnnotatedElement annotatedElement;

  private Map<Class<? extends Annotation>, DomainAnnotation> map =
      new HashMap<Class<? extends Annotation>, DomainAnnotation>();

  private DomainAnnotatedElement(AnnotatedElement annotatedElement) {
    this.annotatedElement = annotatedElement;
  }

  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return getDomainAnnotation(annotationClass).annotation();
  }

  public Annotation[] getAnnotations() {
    return annotatedElement.getAnnotations();
  }

  public Annotation[] getDeclaredAnnotations() {
    return annotatedElement.getDeclaredAnnotations();
  }

  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return annotatedElement.isAnnotationPresent(annotationClass) || isDomainAnnotationPresent(annotationClass);
  }

  /**
   * Returns this element's domain annotation for the specified type if such an
   * annotation is present, else null.
   *
   * @param annotationType
   *          the annotation type
   * @return the domain annotation present on this element.
   */
  public <T extends Annotation> DomainAnnotation<T> getDomainAnnotation(Class<T> annotationType) {
    if (annotatedElement.isAnnotationPresent(annotationType)) {
      return new DomainAnnotationImpl<T>(getAnnotation(annotationType));
    }
    if (map.containsKey(annotationType)) {
      return map.get(annotationType);
    }
    Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
    DomainAnnotation found;
    for (Annotation annotation : getDeclaredAnnotations()) {
      Class<? extends Annotation> type = annotation.annotationType();
      DomainAnnotation parent;
      if (!annotations.contains(type)) {
        parent = new DomainAnnotationImpl(annotation);
        if (type.isAnnotationPresent(annotationType)) {
          return new DomainAnnotationImpl<T>(type.getAnnotation(annotationType), parent);
        }
        found = search(annotations, parent, annotation, annotationType);
        annotations.add(type);
        if (found != null) {
          map.put(annotationType, found);
          return found;
        }
      }
    }
    return null;
  }

  private static <T extends Annotation> DomainAnnotation search(Set annotations, DomainAnnotation parent,
                                                                Annotation annotation, Class<T> annotationType) {
    if (!annotations.contains(annotation.annotationType())) {
      annotations.add(annotation.annotationType());
      parent = new DomainAnnotationImpl(annotation, parent);
      for (Annotation declaredAnnotation : annotation.annotationType().getDeclaredAnnotations()) {
        Class<? extends Annotation> type = declaredAnnotation.annotationType();
        if (type.isAnnotationPresent(annotationType)) {
          return new DomainAnnotationImpl<Annotation>(type.getAnnotation(annotationType), parent);
        }
        DomainAnnotation<T> found = search(annotations, parent, annotation, annotationType);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  public boolean isDomainAnnotationPresent(Class<? extends Annotation> annotationType) {
    return getDomainAnnotation(annotationType) != null;
  }

  public AnnotatedElement annotatedElement() {
    return annotatedElement;
  }

  public static DomainAnnotatedElement wrap(AnnotatedElement annotatedElement) {
    return new DomainAnnotatedElement(annotatedElement);
  }

}
