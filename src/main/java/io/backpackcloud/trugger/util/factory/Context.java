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

package io.backpackcloud.trugger.util.factory;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface that defines a context to create a object. A context indicates
 * which object can be used as an argument to invoke a constructor.
 *
 * @since 5.0
 */
public interface Context {

  /**
   * Adds the given object to the context by binding it to the given predicate.
   *
   * @param object the object to add
   * @return a component to select the condition
   */
  default Mapper use(Object object) {
    return use((parameter) -> object);
  }

  /**
   * Adds the object supplied by the given supplier by binding it to the given
   * predicate.
   *
   * @param supplier the supplier to create the object
   * @return a component to select the condition
   */
  default Mapper use(Supplier supplier) {
    return use(parameter -> supplier.get());
  }

  /**
   * Adds the object returned by the given function by binding it to the given
   * predicate.
   *
   * @param function the function to use
   * @return a component to select the condition
   */
  Mapper use(Function<Parameter, Object> function);

  /**
   * Tries to resolve the given parameter to a object using the predicates
   * added to the context.
   *
   * @param parameter the parameter to resolve the value
   * @return the resolved value.
   */
  Optional<Object> resolve(Parameter parameter);

  /**
   * Interface for mapping the context value to a predicate.
   */
  interface Mapper {

    /**
     * Use the given condition to map the value.
     *
     * @param condition the condition to use
     * @return a reference to the object for doing other mappings.
     */
    Context when(Predicate<? super Parameter> condition);

    /**
     * Uses the mapped value as the default.
     *
     * @return a reference to the object for doing other mappings.
     */
    Context byDefault();

  }

}
