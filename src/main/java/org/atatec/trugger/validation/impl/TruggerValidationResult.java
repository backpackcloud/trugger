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
package org.atatec.trugger.validation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.ValidationResult;

/**
 * This class represents a result of a validation.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerValidationResult implements ValidationResult {
  
  private final Map<String, InvalidElement> invalidElements;
  
  private final Object target;
  
  /**
   * Creates a new ValidationResult
   * 
   * @param invalids
   *          the invalid elements
   * @param target
   *          the validated object
   */
  public TruggerValidationResult(Collection<InvalidElement> invalids, Object target) {
    this.invalidElements = new HashMap<String, InvalidElement>(invalids.size());
    for (InvalidElement invalidElement : invalids) {
      this.invalidElements.put(invalidElement.path(), invalidElement);
    }
    this.target = target;
  }
  
  public Object target() {
    return target;
  }
  
  public Collection<InvalidElement> invalidElements() {
    return new ArrayList<InvalidElement>(invalidElements.values());
  }
  
  public InvalidElement invalidElement(String name) {
    return invalidElements.get(name);
  }
  
  public boolean isValid() {
    return invalidElements.isEmpty();
  }
  
  public boolean isInvalid() {
    return !isValid();
  }
  
}
