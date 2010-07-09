/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.predicate;

import net.sf.trugger.Result;
import net.sf.trugger.validation.ValidationResult;

/**
 * Interface that defines the restrictions allowed by the Predicate DSL.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The object type.
 * @param <V>
 *          The value type
 * @since 2.5
 */
public interface Criteria<E, V> {

  /**
   * The expected value should be equal than the given value.
   *
   * @param value
   *          the restriction value.
   */
  void equal(V value);

  /**
   * The expected value should be equal than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void eq(V value);

  /**
   * The expected value should be different from the given value.
   *
   * @param value
   *          the restriction value.
   */
  void differ(V value);

  /**
   * The expected value should be different from the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void diff(V value);

  /**
   * The expected value should be less than the given value.
   *
   * @param value
   *          the restriction value.
   */
  void lessThan(V value);

  /**
   * The expected value should be less than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void lt(V value);

  /**
   * The expected value should be equal or less than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void lessOrEqual(V value);

  /**
   * The expected value should be equal or less than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void le(V value);

  /**
   * The expected value should be greater than the given value.
   *
   * @param value
   *          the restriction value.
   */
  void greaterThan(V value);

  /**
   * The expected value should be greater than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void gt(V value);

  /**
   * The expected value should be equal or greater than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void greaterOrEqual(V value);

  /**
   * The expected value should be equal or greater than the given value.
   *
   * @param value
   *          the restriction value.
   * @since 2.7
   */
  void ge(V value);

  /**
   * The expected value should match the given value.
   * <p>
   * In this case, the
   *
   * @param pattern
   *          the restriction pattern.
   */
  void matches(String pattern);

  /**
   * The expected value should be valid or invalid based on the given DSL for
   * validation.
   *
   * @param result
   *          the DSL for validate.
   * @return the component for defining the validation result (valid or
   *         invalid).
   */
  ValidationCriteria using(Result<ValidationResult, Object> result);

  /**
   * The expected value should be invalid based on all of its elements.
   */
  void invalid();

  /**
   * The expected value should be valid based on all of its elements.
   */
  void valid();

  /**
   * Interface for defining the validation criteria.
   *
   * @author Marcelo Varella Barca Guimarães
   * @since 2.5
   */
  public static interface ValidationCriteria {

    /**
     * The expected value should be valid.
     */
    void valid();

    /**
     * The expected value should be invalid.
     */
    void invalid();

  }

}