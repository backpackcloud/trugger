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

package tools.devnull.trugger.validation.validator;

import tools.devnull.trugger.ObjectMapper;
import tools.devnull.trugger.reflection.ClassPredicates;
import tools.devnull.trugger.util.Utils;
import tools.devnull.trugger.validation.Validator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import static tools.devnull.trugger.reflection.ClassPredicates.arrayType;
import static tools.devnull.trugger.reflection.ClassPredicates.primitiveArrayType;

/**
 * A composite validator that selects the most suitable validator based on the
 * type of the target value.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class MultiTypeValidator implements Validator {

  private Map<Predicate<Class>, Validator> map = new LinkedHashMap<>();
  private Validator generic = value -> {
    throw new IllegalArgumentException("Cannot determine validator to use " +
        "for type " + Utils.resolveType(value));
  };

  /**
   * Maps a validator to use when the value is assignable to the given type.
   *
   * @param type the type to trigger the validator
   * @param <E>  the type
   * @return a component to select the validator.
   */
  public final <E> ObjectMapper<Validator<E>, MultiTypeValidator> map(Class<E> type) {
    return value -> {
      map.put(ClassPredicates.assignableTo(type), value);
      return MultiTypeValidator.this;
    };
  }

  /**
   * Maps a validator to use when the value is an array.
   *
   * @return a component to select the validator.
   */
  public final ObjectMapper<Validator<Object[]>, MultiTypeValidator> mapArray() {
    return value -> {
      map.put(arrayType().and(primitiveArrayType().negate()), value);
      return MultiTypeValidator.this;
    };
  }

  /**
   * Maps a validator to the other object types.
   *
   * @return a component to select the validator.
   */
  public final ObjectMapper<Validator<Object>, MultiTypeValidator> mapOthers() {
    return value -> {
      generic = value;
      return MultiTypeValidator.this;
    };
  }

  @Override
  public final boolean isValid(Object value) {
    if (value == null) {
      // do not validate null values
      return true;
    }
    Class type = Utils.resolveType(value);
    for (Map.Entry<Predicate<Class>, Validator> entry : map.entrySet()) {
      if (entry.getKey().test(type)) {
        return entry.getValue().isValid(value);
      }
    }
    return generic.isValid(value);
  }

}
