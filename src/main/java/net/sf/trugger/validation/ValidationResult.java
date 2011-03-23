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
package net.sf.trugger.validation;

import java.util.Collection;

/**
 * Interface that represents a result of a validation.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface ValidationResult {
  
  /**
   * @return the validated object.
   */
  Object target();
  
  /**
   * @return the invalid elements.
   */
  Collection<InvalidElement> invalidElements();
  
  /**
   * @param pathToElement
   *          the path to the invalid element (including the element name).
   * @return the invalid element in the given path.
   */
  InvalidElement invalidElement(String pathToElement);
  
  /**
   * @return <code>true</code> if the target is valid.
   */
  boolean isValid();
  
  /**
   * @return <code>true</code> if the target is invalid.
   */
  boolean isInvalid();
}
