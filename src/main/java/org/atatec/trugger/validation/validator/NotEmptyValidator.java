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
 * @since 5.1
 */
public class NotEmptyValidator implements Validator {

  @Override
  public boolean isValid(@NotNull Object value) {
    if (value instanceof CharSequence)
      return isValid((CharSequence) value);
    if (value instanceof Collection)
      return isValid((Collection) value);
    if (value instanceof Map)
      return isValid((Map) value);
    return Array.getLength(value) > 0;
  }

  private boolean isValid(CharSequence string) {
    return string.length() > 0;
  }

  private boolean isValid(Collection collection) {
    return !collection.isEmpty();
  }

  private boolean isValid(Map map) {
    return !map.isEmpty();
  }

}
