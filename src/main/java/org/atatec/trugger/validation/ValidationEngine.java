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
package org.atatec.trugger.validation;

import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;

/**
 * Interface that defines a class capable of validate an object based on the
 * Metadatas of its properties.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface ValidationEngine {

  /**
   * Selects the elements for validation. Only the specified elements will be
   * validated.
   *
   * @param selector
   *          the elements that must be validated.
   * @return the component for doing the validation.
   */
  Result<ValidationResult, Object> selection(ElementsSelector selector);

  /**
   * Selects the element for validation. Only the specified element will be
   * validated.
   *
   * @param selector
   *          the element that must be validated.
   * @return the component for doing the validation.
   */
  Result<ValidationResult, Object> selection(ElementSelector selector);

  /**
   * Indicates that the validation must be processed only over the annotations
   * with property <code>context</code> equals to given context.
   *
   * @param context
   *          the context validation.
   * @return a reference to this object.
   */
  ValidationEngine forContext(String context);

  /**
   * Selects all properties for validation.
   *
   * @return the component for doing the validation.
   */
  Result<ValidationResult, Object> allElements();

  /**
   * Selects the given element for validation.
   *
   * @param element
   *          the element for validation.
   * @return the component for doing the validation.
   */
  Result<ValidationResult, Object> element(Element element);

}
