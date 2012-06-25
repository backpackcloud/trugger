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
package org.atatec.trugger.validation.validator;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.ValidationBridge;
import org.atatec.trugger.validation.ValidationResult;

/**
 * Interface that defines a strategy for validate objects recursively. This is
 * used by the {@link ValidValidator} an can be plugged using the {@link Valid}
 * annotation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public interface ValidationStrategy {

  /**
   * Indicates if the validation should stop on the first invalid object. Used
   * if the object belongs to a collection.
   *
   * @return <code>true</code> if the validation should stop on the first
   *         invalid object.
   */
  boolean breakOnFirstInvalidObject();

  /**
   * Validates an object.
   *
   * @param object
   *          the object to validate.
   * @param bridge
   *          the bridge to interact with the validation.
   * @param annotation
   *          the annotation used in the element.
   * @param element
   *          the element that contains the object.
   * @return the result of the validation.
   */
  ValidationResult validate(Object object, ValidationBridge bridge, Valid annotation, Element element);

  /**
   * Validates an object from a collection.
   *
   * @param index
   *          the index of the element
   * @param object
   *          the object to validate.
   * @param bridge
   *          the bridge to interact with the validation.
   * @param annotation
   *          the annotation used in the element.
   * @param element
   *          the element that contains the object.
   * @return the result of the validation.
   */
  ValidationResult validateInCollection(int index, Object object, ValidationBridge bridge, Valid annotation,
      Element element);

}
