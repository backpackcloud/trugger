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

import org.atatec.trugger.ObjectMapper;
import org.atatec.trugger.reflection.ClassPredicates;
import org.atatec.trugger.util.Utils;
import org.atatec.trugger.validation.Validator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.atatec.trugger.reflection.ClassPredicates.arrayType;
import static org.atatec.trugger.reflection.ClassPredicates.primitiveArrayType;

/**
 * A composite validator that selects the most suitable validator based on the
 * target value's type.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class TypedCompositeValidator implements Validator {

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
  public <E> ObjectMapper<Validator<E>, TypedCompositeValidator> map(Class<E> type) {
    return value -> {
      map.put(ClassPredicates.assignableTo(type), value);
      return TypedCompositeValidator.this;
    };
  }

  /**
   * Maps a validator to use when the value is an array.
   *
   * @return a component to select the validator.
   */
  public ObjectMapper<Validator<Object[]>, TypedCompositeValidator> mapArray() {
    return value -> {
      map.put(arrayType().and(primitiveArrayType().negate()), value);
      return TypedCompositeValidator.this;
    };
  }

  /**
   * Maps a validator to the other object types.
   *
   * @return a component to select the validator.
   */
  public ObjectMapper<Validator<Object>, TypedCompositeValidator> mapOthers() {
    return value -> {
      generic = value;
      return TypedCompositeValidator.this;
    };
  }

  @Override
  public boolean isValid(Object value) {
    Class type = Utils.resolveType(value);
    for (Map.Entry<Predicate<Class>, Validator> entry : map.entrySet()) {
      if (entry.getKey().test(type)) {
        return entry.getValue().isValid(value);
      }
    }
    return generic.isValid(value);
  }

}
