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

/**
 * Validator that checks a minimum size requirement.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class MinValidator implements Validator {

  private final Validator sizeValidator;

  public MinValidator(double value, boolean inclusive, double delta) {
    this.sizeValidator = new SizeValidator(
        size -> inclusive ? size + delta >= value : size + delta > value
    );
  }

  @Override
  public boolean isValid(Object value) {
    return sizeValidator.isValid(value);
  }

}
