/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package com.backpackcloud.trugger.util.impl;

import com.backpackcloud.trugger.util.PredicateMappingFunction;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * An implementation of a PredicateMappingFunction
 *
 * @author Marcelo Guimaraes
 */
public class PredicateMappingFunctionImpl<T, R> implements PredicateMappingFunction<T, R> {

  private final Function<? super T, ? extends R> defaultFunction;
  private final Predicate<? super T> predicate;
  private final Function<? super T, ? extends R> function;

  /**
   * Creates a new function using the given one as the default
   *
   * @param defaultFunction the default function to use
   */
  public PredicateMappingFunctionImpl(Function<? super T, ? extends R> defaultFunction) {
    this(defaultFunction, t -> false, null);
  }

  /**
   * Creates a new function without a default function.
   */
  public PredicateMappingFunctionImpl() {
    this(doThrow(IllegalArgumentException::new), t -> false, null);
  }

  /**
   * Creates a new function with the given parameters
   *
   * @param defaultFunction the default function
   * @param predicate       the predicate to test the input
   * @param function        the function to execute if the predicate matches the input
   */
  private PredicateMappingFunctionImpl(Function<? super T, ? extends R> defaultFunction,
                                       Predicate<? super T> predicate,
                                       Function<? super T, ? extends R> function) {
    this.defaultFunction = defaultFunction;
    this.predicate = predicate;
    this.function = function;
  }

  @Override
  public Mapper<T, R> when(Predicate<? super T> condition) {
    return f -> new PredicateMappingFunctionImpl<>(this, condition, f);
  }

  @Override
  public R apply(T t) {
    if (predicate.test(t)) {
      return function.apply(t);
    }
    return defaultFunction.apply(t);
  }

  /**
   * Returns a function that throws the exception supplied by the given supplier.
   *
   * @param exceptionSupplier the supplier to supply the exceptions to throw
   * @return a function that throws exceptions regardless the input
   */
  private static <T, R> Function<T, R> doThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
    return t -> {
      throw exceptionSupplier.get();
    };
  }

}
