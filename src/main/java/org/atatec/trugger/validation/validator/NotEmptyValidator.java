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

import org.atatec.trugger.validation.Validator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Validator for objects that cannot be empty.
 * <p>
 * This validator applies to CharSequences (like strings), Collections,
 * Maps and Arrays.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class NotEmptyValidator implements Validator {

  private final TypedCompositeValidator validator;

  public NotEmptyValidator() {
    this.validator = new TypedCompositeValidator();
    initialize();
  }

  private void initialize() {
    validator.map(CharSequence.class).to(string -> string.length() > 0)
        .map(Collection.class).to(collection -> collection.size() > 0)
        .map(Map.class).to(map -> map.size() > 0)
        .mapArray().to(array -> array.length > 0)
        .mapOthers().to(array -> Array.getLength(array) > 0);
  }

  @Override
  public boolean isValid(@NotNull Object value) {
    return validator.isValid(value);
  }

}
