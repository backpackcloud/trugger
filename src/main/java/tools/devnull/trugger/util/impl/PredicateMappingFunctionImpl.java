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

package tools.devnull.trugger.util.impl;

import tools.devnull.trugger.util.PredicateMappingFunction;

import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateMappingFunctionImpl<T, R> implements PredicateMappingFunction<T, R> {

  private final Function<? super T, ? extends R> defaultFunction;
  private final Predicate<? super T> predicate;
  private final Function<? super T, ? extends R> function;

  public PredicateMappingFunctionImpl(Function<? super T, ? extends R> defaultFunction,
                               Predicate<? super T> predicate,
                               Function<? super T, ? extends R> function) {
    this.defaultFunction = defaultFunction;
    this.predicate = predicate;
    this.function = function;
  }

  @Override
  public Mapper<T, R> use(Function<? super T, ? extends R> function) {
    return condition -> new PredicateMappingFunctionImpl<>(this, condition, function);
  }

  @Override
  public R apply(T t) {
    if (predicate.test(t)) {
      return function.apply(t);
    }
    return defaultFunction.apply(t);
  }

}
