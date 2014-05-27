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
import java.util.function.Function;

/**
 * Base class for validators that compares object lengths and sizes.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class SizeValidator implements Validator {

  private final MultiTypeValidator validator;
  private final Function<Double, Boolean> function;

  public SizeValidator(Function<Double, Boolean> checkFunction) {
    this.function = checkFunction;
    this.validator = new MultiTypeValidator();
    initialize();
  }

  private void initialize() {
    validator.map(Number.class).to(number -> checkValue(number.doubleValue()))
        .map(Collection.class).to(collection -> checkValue(collection.size()))
        .map(Map.class).to(map -> checkValue(map.size()))
        .map(CharSequence.class).to(string -> checkValue(string.length()))
        .mapArray().to(array -> checkValue(array.length))
        // assume that any other type is a primitive array type
        .mapOthers().to(array -> checkValue(Array.getLength(array)));
  }

  private boolean checkValue(double value) {
    return function.apply(value);
  }

  @Override
  public final boolean isValid(@NotNull Object value) {
    return validator.isValid(value);
  }

}
