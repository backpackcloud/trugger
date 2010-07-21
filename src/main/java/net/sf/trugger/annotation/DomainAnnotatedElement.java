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
package net.sf.trugger.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.annotation.impl.DomainAnnotationImpl;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class DomainAnnotatedElement implements AnnotatedElement {

  private final AnnotatedElement annotatedElement;

  public DomainAnnotatedElement(AnnotatedElement annotatedElement) {
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

  public <T extends Annotation> DomainAnnotation<T> getDomainAnnotation(Class<T> annotationType) {
    if (annotatedElement.isAnnotationPresent(annotationType)) {
      return new DomainAnnotationImpl(getAnnotation(annotationType));
    }
    Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
    DomainAnnotation found = null;
    for (Annotation annotation : getDeclaredAnnotations()) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (!annotations.contains(type)) {
        DomainAnnotation parent = new DomainAnnotationImpl(annotation);
        if (type.isAnnotationPresent(annotationType)) {
          return new DomainAnnotationImpl<T>(type.getAnnotation(annotationType), parent);
        }
        found = search(annotations, parent, annotation, annotationType);
        annotations.add(type);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private <T extends Annotation> DomainAnnotation search(Set annotations, DomainAnnotation parent,
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

  public static DomainAnnotatedElement wrap(AnnotatedElement annotatedElement) {
    return new DomainAnnotatedElement(annotatedElement);
  }

}
