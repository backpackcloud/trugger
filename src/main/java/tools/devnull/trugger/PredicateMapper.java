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

import java.util.function.Predicate;

/**
 * Interface that maps something to a predicate inside a fluent interface.
 *
 * @since 5.0
 */
public interface PredicateMapper<T, R> {

  /**
   * Use the given condition to map the value.
   *
   * @param condition the condition to use
   * @return a reference to the object for doing other mappings.
   */
  R when(Predicate<T> condition);

  /**
   * Uses the mapped value as the default.
   *
   * @return a reference to the object for doing other mappings.
   * @since 5.2
   */
  R byDefault();

}
