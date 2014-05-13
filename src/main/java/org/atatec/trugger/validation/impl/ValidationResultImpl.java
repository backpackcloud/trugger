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

package org.atatec.trugger.validation.impl;

import org.atatec.trugger.element.Elements;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.ValidationResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcelo Guimarães
 */
class ValidationResultImpl implements ValidationResult {

  private final Object target;
  final Map<String, InvalidElement> invalidElements;

  ValidationResultImpl(Object target) {
    this.target = target;
    this.invalidElements = new HashMap<>();
  }

  @Override
  public boolean isInvalid() {
    return !invalidElements.isEmpty();
  }

  @Override
  public boolean isElementInvalid(String name) {
    return invalidElements.containsKey(name);
  }

  @Override
  public Object target() {
    return target;
  }

  @Override
  public InvalidElement invalidElement(String name) {
    return invalidElements.get(name);
  }

  @Override
  public Collection<InvalidElement> invalidElements() {
    return Collections.unmodifiableCollection(invalidElements.values());
  }

  void add(int index, Object value, ValidationResultImpl result) {
    for (InvalidElement element : result.invalidElements()) {
      String name = String.format("%d.%s", index, element.name());
      InvalidElement invalid = new InvalidElementImpl(
          Elements.element(name).in(target),
          value,
          element.violatedConstraints()
      );
      invalidElements.put(name, invalid);
    }
  }

}
