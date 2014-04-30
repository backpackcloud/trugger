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

package org.atatec.trugger.validation;

/**
 * Interface that defines a validator for objects.
 *
 * @since 5.1
 */
public interface Validator<T> {

  /**
   * Validates the value according to the constraints defined by this validator.
   *
   * @param value the value to validate
   * @return <code>true</code> if the value is valid, <code>false</code>
   * otherwise.
   */
  boolean isValid(T value);

  /**
   * Bind this validator and the given one using the boolean operation
   * <code>and</code>.
   *
   * @param other the other validator
   * @return a validator that returns <code>true</code> if this validator and
   * the given one returns <code>true</code> for the given value.
   */
  default Validator and(Validator other) {
    return obj -> this.isValid((T) obj) && other.isValid(obj);
  }

  /**
   * * Bind this validator and the given one using the boolean operation
   * <code>or</code>.
   *
   * @param other the other validator
   * @return a validator that returns <code>true</code> if this validator or
   * the given one returns <code>true</code> for the given value.
   */
  default Validator or(Validator other) {
    return obj -> this.isValid((T) obj) || other.isValid(obj);
  }

}
