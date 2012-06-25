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
import org.atatec.trugger.util.Utils;
import org.atatec.trugger.validation.ValidationBridge;
import org.atatec.trugger.validation.ValidationResult;

/**
 * The default implementation for the {@link ValidationStrategy}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class DefaultValidationStrategy implements ValidationStrategy {

  /**
   * Returns <code>true</code>.
   */
  public boolean breakOnFirstInvalidObject() {
    return true;
  }

  public ValidationResult validateInCollection(int index, Object object, ValidationBridge bridge, Valid annotation,
      Element element) {
    return validate(object, bridge, annotation, element);
  }

  public ValidationResult validate(Object object, ValidationBridge bridge, Valid annotation, Element element) {
    String context = annotation.forContext();
    if (Utils.isEmpty(context)) {
      return bridge.validate().allElements().in(object);
    }
    return bridge.validate().forContext(context).allElements().in(object);
  }

}
