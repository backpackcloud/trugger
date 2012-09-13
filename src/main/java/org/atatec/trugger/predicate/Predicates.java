/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.predicate;

import org.atatec.trugger.Result;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationResult;

/**
 * An utility class to handle {@link Predicate} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class Predicates {

  private Predicates() {
  }

  /** A predicate that always returns <code>true</code>. */
  public static final CompositePredicate ALWAYS_TRUE = wrap(new ConstantPredicate(true));
  /** A predicate that always returns <code>false</code>. */
  public static final CompositePredicate ALWAYS_FALSE = wrap(new ConstantPredicate(false));

  /**
   * Wraps the given predicate into a CompositePredicate
   *
   * @return a composite predicate that uses the given one to evaluate
   *
   * @since 4.1
   */
  public static <T> CompositePredicate<T> wrap(final Predicate<? super T> predicate) {
    return new BasePredicate<T>() {
      @Override
      public boolean evaluate(T element) {
        return predicate.evaluate(element);
      }

      public String toString() {
        return predicate.toString();
      }

    };
  }

  /** @return a predicate that returns the negation of the given one. */
  public static <T> CompositePredicate<T> not(final Predicate<? super T> predicate) {
    return wrap(predicate).negate();
  }

  /**
   * A predicate that returns <code>true</code> if an object is valid based on all of its
   * elements.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> VALID = validUsing(new Validation().validate().allElements());

  /**
   * A predicate that returns <code>true</code> if an object is invalid based on all of
   * its elements.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> INVALID = invalidUsing(new Validation().validate().allElements());

  /**
   * @return a predicate that returns <code>true</code> if an object is valid based on the
   *         given validation result.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> validUsing(final Result<ValidationResult, Object> result) {
    return new BasePredicate<Object>() {

      public boolean evaluate(Object element) {
        return result.in(element).isValid();
      }
    };
  }

  /**
   * @return a predicate that returns <code>true</code> if an object is invalid based on
   *         the given validation result.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> invalidUsing(final Result<ValidationResult, Object> result) {
    return new BasePredicate<Object>() {

      public boolean evaluate(Object element) {
        return result.in(element).isInvalid();
      }
    };
  }

  /**
   * @see #wrap(Predicate)
   * @see #that(CompositePredicate)
   */
  public static <E> CompositePredicate<E> that(Predicate<E> predicate) {
    return wrap(predicate);
  }

  /**
   * Returns the given predicate. This method purpose is to make the code more readable.
   */
  public static <E> CompositePredicate<E> that(CompositePredicate<E> predicate) {
    return predicate;
  }

  /**
   * Returns the given predicate. This method purpose is to make the code more readable.
   */
  public static <E> CompositePredicate<E> is(CompositePredicate<E> predicate) {
    return predicate;
  }

  /**
   * @see #wrap(Predicate)
   * @see #that(CompositePredicate)
   */
  public static <E> CompositePredicate<E> is(Predicate<E> predicate) {
    return wrap(predicate);
  }

  private static class ConstantPredicate implements Predicate<Object> {

    private final boolean returnValue;

    public ConstantPredicate(boolean returnValue) {
      super();
      this.returnValue = returnValue;
    }

    public boolean evaluate(Object element) {
      return returnValue;
    }

    @Override
    public String toString() {
      return String.valueOf(returnValue);
    }
  }

}
