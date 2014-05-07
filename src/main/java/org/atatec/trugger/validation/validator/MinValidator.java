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
public class MinValidator extends BaseSizeValidator implements Validator {

  private final double minValue;
  private final boolean inclusive;
  private final double delta;

  public MinValidator(double value, boolean inclusive, double delta) {
    this.minValue = value;
    this.inclusive = inclusive;
    this.delta = delta;
  }

  public MinValidator(Min constraint) {
    this(constraint.value(), constraint.inclusive(), constraint.delta());
  }

  protected boolean checkValue(double value) {
    return inclusive ? value + delta >= minValue : value + delta > minValue;
  }

}
