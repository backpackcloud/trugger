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

package tools.devnull.trugger.util.factory;

import tools.devnull.trugger.PredicateMapper;

import java.lang.reflect.Parameter;
import java.util.function.Function;
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
  PredicateMapper<Parameter, Context> use(Object object);

  /**
   * Adds the object supplied by the given supplier by binding it to the given
   * predicate.
   *
   * @param supplier the supplier to create the object
   * @return a component to select the condition
   */
  PredicateMapper<Parameter, Context> use(Supplier supplier);

  /**
   * Adds the object returned by the given function by binding it to the given
   * predicate.
   *
   * @param function the function to use
   * @return a component to select the condition
   */
  PredicateMapper<Parameter, Context> use(Function<Parameter, Object> function);

  /**
   * Tries to resolve the given parameter to a object using the predicates
   * added to the context.
   *
   * @param parameter the parameter to resolve the value
   * @return the resolved value.
   * @throws UnresolvableValueException if the value cannot be resolved
   */
  Object resolve(Parameter parameter) throws UnresolvableValueException;

}
