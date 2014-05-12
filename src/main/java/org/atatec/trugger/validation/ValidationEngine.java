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

import org.atatec.trugger.element.Element;

import java.util.function.Predicate;

/**
 * Interface that defines an engine capable of validating elements of an object
 * and summarize it in a result that allows integration with other tools (like
 * web frameworks).
 * <p>
 * This is not a replacement for the Bean Validation spec, is only a way to
 * simplify validations that should be done programatically.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface ValidationEngine {

  /**
   * Validates only the elements that matches the given filter.
   *
   * @param filter the filter to restrict the elements.
   * @return a new ValidationEngine that uses the given filter.
   */
  ValidationEngine filter(Predicate<Element> filter);

  /**
   * Validates all elements in the given target.
   *
   * @param target the target to validate.
   * @return the validation result.
   */
  ValidationResult validate(Object target);

}
