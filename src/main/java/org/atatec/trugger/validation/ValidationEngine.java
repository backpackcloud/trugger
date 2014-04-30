/*
 * Copyright 2009-2014 Marcelo Guimarães
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
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.selector.ElementsSelector;

/**
 * Interface that defines an engine capable of validating elements of an object
 * and summarize it in a result that allows integration with other tools (like
 * web frameworks).
 * <p>
 * This is not a replacement for the Bean Validation spec, is only a way to
 * simplify validations that should be done programatically.
 *
 * @since 5.1
 */
public interface ValidationEngine {

  /**
   * Validates only the elements denoted by the given selector.
   *
   * @param selector the selector to filter the elements.
   * @return the component to specify the validation target and obtaining
   * the validation result.
   */
  Result<ValidationResult, Object> validate(ElementsSelector selector);

  /**
   * Validates all elements in the given target.
   *
   * @param target the target to validate.
   * @return the validation result.
   */
  default ValidationResult validate(Object target) {
    return validate(Elements.elements()).in(target);
  }

}
