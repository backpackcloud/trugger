/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.devnull.trugger;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Holds a value, that may be or not a null-value.
 *
 * @param <E> the value's type
 */
public interface Optional<E> {

  /**
   * Returns the result of the rest invocation.
   *
   * @return the result of the rest invocation.
   */
  E value();

  /**
   * Filters this result by testing the value against the
   * given predicate.
   * <p>
   * If the predicate matches the value, then the same instance
   * will be returned.
   *
   * @param predicate the predicate to test
   * @return an empty result if the predicate doesn't matches the value
   */
  default Optional<E> filter(Predicate<? super E> predicate) {
    E value = value();
    if (value != null) {
      return predicate.test(value) ? this : Optional.empty();
    }
    return this;
  }

  /**
   * Checks if this optional holds a non-null value.
   *
   * @return {@code true} if the value is not null
   */
  default boolean containsValue() {
    return value() != null;
  }

  /**
   * Invokes the given consumer passing the result.
   * <p>
   * The consumer will be invoked only if this result
   * contains a non-null value.
   *
   * @param consumer the consumer to use
   * @return an instance of this object.
   */
  default Optional<E> and(Consumer<E> consumer) {
    E value = value();
    if (value != null) {
      consumer.accept(value);
    }
    return this;
  }

  /**
   * Maps the value using the given function and return a new
   * result containing the new value.
   * <p>
   * The function will be invoked only if this result
   * contains a non-null value.
   *
   * @param function the function to map the value
   * @return a result containing the new value
   */
  default <T> Optional<T> map(Function<? super E, ? extends T> function) {
    E value = value();
    if (value != null) {
      return Optional.of(function.apply(value));
    }
    return Optional.empty();
  }

  /**
   * Executes the given action in case of no result from the REST invocation
   *
   * @param action the action to execute.
   */
  default void orElse(Runnable action) {
    if (value() == null) {
      action.run();
    }
  }

  /**
   * Uses the given supplier to return a value in case of no result from the REST invocation
   *
   * @param supplier the supplier to use for retrieving the result
   * @return this optional value or the value returned by the supplier.
   */
  default E orElseReturn(Supplier<E> supplier) {
    E value = value();
    return value != null ? value : supplier.get();
  }

  /**
   * Throws the supplied exception if case of no result from the REST invocation
   *
   * @param exceptionSupplier the exception supplier
   * @return the result
   */
  default E orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
    E value = value();
    if (value == null) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  /**
   * Creates a simple result containing the given value
   *
   * @param value the value of the result
   * @return a result object that holds the given value
   */
  static <E> Optional<E> of(E value) {
    return () -> value;
  }

  /**
   * Wraps the given supplier as a Result object.
   *
   * @param supplier the supplier to get the value
   * @return the given supplier wrapped as a result.
   */
  static <E> Optional<E> of(Supplier<E> supplier) {
    return supplier::get;
  }

  /**
   * Creates a result of a null value.
   *
   * @return a result of a null value.
   */
  static <E> Optional<E> empty() {
    return () -> null;
  }

  /**
   * Creates a result of a null value
   *
   * @param type the type of the value
   * @return a result of a null value
   */
  static <E> Optional<E> empty(Class<E> type) {
    return () -> null;
  }

}
