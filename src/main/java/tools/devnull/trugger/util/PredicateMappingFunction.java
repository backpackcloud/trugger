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

package tools.devnull.trugger.util;

import tools.devnull.trugger.util.impl.PredicateMappingFunctionImpl;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Function that can be mapped to a Predicate to only execute if the
 * {@link Predicate#test(Object)} is {@code true}.
 *
 * @since 5.3
 */
public interface PredicateMappingFunction<T, R> extends Function<T, R> {

  /**
   * Maps a function to a predicate.
   *
   * @param function the function to map
   * @return a component to define the predicate
   */
  Mapper<T, R> use(Function<? super T, ? extends R> function);

  /**
   * Interface that maps a function to a predicate.
   */
  interface Mapper<T, R> {
    PredicateMappingFunction<T, R> when(Predicate<? super T> condition);
  }

  static <T, R> PredicateMappingFunction<T, R> byDefault(Function<? super T, ? extends R> function) {
    return new PredicateMappingFunctionImpl<>(null, t -> true, function);
  }

  static <T, R> PredicateMappingFunction<T, R> begin() {
    return new PredicateMappingFunctionImpl<>(o -> {
      throw new IllegalArgumentException();
    }, t -> false, null);
  }

}
