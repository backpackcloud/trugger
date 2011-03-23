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
package net.sf.trugger.validation.validator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import net.sf.trugger.annotation.AcceptArrays;
import net.sf.trugger.annotation.AcceptedTypes;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.Validator;

/**
 * Implementation of the {@link NotEmpty} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
@AcceptArrays
@AcceptedTypes( { Collection.class, Map.class, CharSequence.class })
public class NotEmptyValidator implements Validator<Object> {

  public boolean isValid(Object value) {
    if (value == null) {
      return false;
    }
    if (value instanceof CharSequence) {
      return !Utils.isEmpty(value.toString());
    } else if (value instanceof Collection) {
      return !((Collection) value).isEmpty();
    } else if (value instanceof Map) {
      return !((Map) value).isEmpty();
    } else {
      return Array.getLength(value) > 0;
    }
  }

}
