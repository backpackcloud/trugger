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

package org.atatec.trugger.validation.validator;

import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.Validator;

import java.util.Collection;
import java.util.Map;

/**
 * Validator for items of an array, a collection or a map.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class ValidsValidator implements Validator {

  private final ValidationEngine engine;

  public ValidsValidator() {
    this(Validation.engine());
  }

  public ValidsValidator(ValidationEngine engine) {
    this.engine = engine;
  }

  @Override
  public boolean isValid(@NotNull Object value) {
    if (value instanceof Collection) {
      return isValid((Collection) value);
    } else if (value instanceof Map) {
      return isValid(((Map) value).values());
    } else { // assume an array type
      return isValid((Object[]) value);
    }
  }

  private boolean isValid(Object[] value) {
    for (Object o : value) {
      if (engine.validate(o).isInvalid()) {
        return false;
      }
    }
    return true;
  }

  private boolean isValid(Collection value) {
    for (Object o : value) {
      if (engine.validate(o).isInvalid()) {
        return false;
      }
    }
    return true;
  }

}
