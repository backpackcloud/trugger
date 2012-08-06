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
import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationResult;

/**
 * An utility class to handle {@link Predicate} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class Predicates {

  private static PredicateFactory factory;

  private Predicates() {}

  static {
    factory = ImplementationLoader.getInstance().get(PredicateFactory.class);
  }

  /**
   * A predicate that always returns <code>true</code>.
   */
  public static final CompositePredicate ALWAYS_TRUE = is(new ConstantPredicate(true));
  /**
   * A predicate that always returns <code>false</code>.
   */
  public static final CompositePredicate ALWAYS_FALSE = is(new ConstantPredicate(false));

  /**
   * Note: This method returns the {@link #ALWAYS_TRUE} field, but with type
   * safety.
   *
   * @return A predicate that always returns <code>true</code>.
   */
  public static <T> CompositePredicate<T> alwaysTrue() {
    return ALWAYS_TRUE;
  }

  /**
   * Note: This method returns the {@link #ALWAYS_FALSE} field, but with type
   * safety.
   *
   * @return A predicate that always returns <code>false</code>.
   */
  public static <T> CompositePredicate<T> alwaysFalse() {
    return ALWAYS_FALSE;
  }

  /**
   * @param value
   *          the value that must be returned by the predicate.
   * @return a predicate that always return the given value.
   */
  public static <T> CompositePredicate<T> valueOf(boolean value) {
    return value ? ALWAYS_TRUE : ALWAYS_FALSE;
  }

  /**
   * @return a predicate that returns the negation of the given one.
   */
  public static <T> CompositePredicate<T> not(Predicate<? super T> predicate) {
    return Predicates.<T>is(predicate).negate();
  }

  /**
   * @return a predicate that returns the negation of the given one.
   */
  public static <T> CompositePredicate<T> not(CompositePredicate<T> predicate) {
    return predicate.negate();
  }

  /**
   * Creates a new {@link CompositePredicate} using a {@link PredicateFactory}
   * to create the instance.
   *
   * @param predicate
   *          the initial predicate of the composition.
   * @return the created predicate.
   */
  public static <T> CompositePredicate<T> is(Predicate<? super T> predicate) {
    return factory.createCompositePredicate(predicate);
  }

  /**
   * A predicate that returns <code>true</code> if an object is valid based on
   * all of its elements.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> VALID = validUsing(new Validation().validate().allElements());

  /**
   * A predicate that returns <code>true</code> if an object is invalid based on
   * all of its elements.
   *
   * @since 2.5
   */
  public static CompositePredicate<Object> INVALID = invalidUsing(new Validation().validate().allElements());

  /**
   * @return a predicate that returns <code>true</code> if an object is valid
   *         based on the given validation result.
   * @since 2.5
   */
  public static CompositePredicate<Object> validUsing(final Result<ValidationResult, Object> result) {
    return is(new Predicate<Object>() {

      public boolean evaluate(Object element) {
        return result.in(element).isValid();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if an object is invalid
   *         based on the given validation result.
   * @since 2.5
   */
  public static CompositePredicate<Object> invalidUsing(final Result<ValidationResult, Object> result) {
    return is(new Predicate<Object>() {

      public boolean evaluate(Object element) {
        return result.in(element).isInvalid();
      }
    });
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
