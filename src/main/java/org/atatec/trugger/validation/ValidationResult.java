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

import java.util.Collection;

/**
 * Interface that defines a validation result.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface ValidationResult {

  /**
   * @return the target object.
   */
  Object target();

  /**
   * @return the invalid elements of the target.
   */
  Collection<InvalidElement> invalidElements();

  /**
   * @param name the element name
   * @return the invalid element that has the given name or <code>null</code>
   * if the element is valid or does not exists in the target.
   */
  InvalidElement invalidElement(String name);

  /**
   * @param name the element name
   * @return <code>true</code> if there is an invalid element with the given
   * name, <code>false</code> otherwise.
   */
  boolean isElementInvalid(String name);

  boolean isValid();

  default boolean isInvalid() {
    return !isValid();
  }

}
