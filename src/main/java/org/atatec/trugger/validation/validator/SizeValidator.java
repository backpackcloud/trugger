/*
 * Copyright 2009-2012 Marcelo Guimarães
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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.atatec.trugger.annotation.AcceptArrays;
import org.atatec.trugger.annotation.AcceptedTypes;
import org.atatec.trugger.validation.Validator;

/**
 * Implementation of the {@link Size} validation.
 * 
 * @author Marcelo Guimarães
 * @since 2.1
 */
@AcceptArrays
@AcceptedTypes(value = { Map.class, Collection.class })
public class SizeValidator implements Validator<Object> {
  
  private Size annotation;
  
  public boolean isValid(@NotNull Object value) {
    int length;
    if (value instanceof Collection) {
      length = ((Collection<?>) value).size();
    } else if (value instanceof Map) {
      length = ((Map<?, ?>) value).size();
    } else {
      length = Array.getLength(value);
    }
    return (length >= Math.abs(annotation.min())) && (length <= Math.abs(annotation.max()));
  }
}
