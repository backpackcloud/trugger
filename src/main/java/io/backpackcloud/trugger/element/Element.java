/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger.element;

import io.backpackcloud.trugger.HandlingException;
import io.backpackcloud.trugger.ValueHandler;

import java.lang.reflect.AnnotatedElement;

/**
 * Interface that defines an element of a class or an object.
 *
 * @author Marcelo Guimaraes
 * @since 1.2
 */
public interface Element extends AnnotatedElement, ValueHandler {

  /**
   * @return the Class object representing the class or interface that declares
   * the element.
   */
  Class declaringClass();

  /**
   * @return the element name
   */
  String name();

  /**
   * @return the element type
   */
  Class type();

  /**
   * @return <code>true</code> if the value of this element can be read.
   */
  boolean isReadable();

  /**
   * @return <code>true</code> if the value of this element can be change.
   */
  boolean isWritable();

  /**
   * Handles this element on the given target (if applies).
   * <p>
   * This method may throw exceptions in case of the element cannot be handled
   * or an error occurring while handling the element.
   *
   * @param target the target that contains this element.
   * @return a component for handling the value of this element in the given
   * target.
   */
  ValueHandler on(Object target);

  /**
   * Checks if this element is specific for a target. If this method returns
   * <code>true</code>, then the methods {@link #getValue()} and
   * {@link #setValue(Object)} can be used.
   *
   * @return <code>true</code> if this element is specific for a target.
   * @since 2.0
   */
  boolean isSpecific();

  /**
   * Returns the target for this element in case of a {@link #isSpecific()
   * specific} one.
   *
   * @return the target for this element if it is {@link #isSpecific() specific}
   * or <code>null</code> if not.
   */
  <E> E target();

  /**
   * Returns the value if this element is {@link #isSpecific() specific}.
   *
   * @throws NonSpecificElementException if the element is not specific
   */
  @Override
  default <E> E getValue() throws HandlingException {
    if (isSpecific()) {
      return on(target()).getValue();
    }
    throw new NonSpecificElementException();
  }

  /**
   * Sets the value if this element is {@link #isSpecific() specific}.
   *
   * @throws NonSpecificElementException if the element is not specific
   */
  @Override
  default void setValue(Object value) throws HandlingException {
    if (isSpecific()) {
      on(target()).setValue(value);
    } else {
      throw new NonSpecificElementException();
    }
  }

}
