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
package org.atatec.trugger.annotation.impl;

import java.lang.annotation.Annotation;

import org.atatec.trugger.annotation.DomainAnnotation;

/**
 * @author Marcelo Guimarães
 * @since 2.7
 */
public class DomainAnnotationImpl<T extends Annotation> implements DomainAnnotation<T> {

  private final T annotation;

  private final DomainAnnotation parent;

  public DomainAnnotationImpl(T annotation) {
    this(annotation, null);
  }

  public DomainAnnotationImpl(T annotation, DomainAnnotation parent) {
    this.annotation = annotation;
    this.parent = parent;
  }

  @Override
  public DomainAnnotation parent() {
    return parent;
  }

  @Override
  public T annotation() {
    return annotation;
  }

}
