/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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

/**
 * Interface that defines a context for operations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public interface AnnotationFactoryContext {

  /**
   * @return the annotation for creating the component.
   */
  Annotation annotation();

  /**
   * @return the annotated element. If it is an {@link Element} instance, then
   *         the {@link #element()} method must return the same object.
   */
  AnnotatedElement annotatedElement();

  /**
   * @return the element being processed or <code>null</code> if there is no
   *         one.
   */
  Element element();

  /**
   * @return the target for processing the binds or <code>null</code> if there
   *         is no one.
   */
  Object target();

}
