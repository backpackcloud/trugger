/*
 * Copyright 2009-2012 Marcelo Guimarães
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

package org.atatec.trugger.validation;

/**
 * Interface that defines an invoker for a validator.
 *
 * @author Marcelo Guimarães
 * @since 3.0
 */
public interface ValidatorInvoker<T> extends Validator<T> {

  /** Invokes the {@link Validator#isValid(Object)} method on the target validator. */
  boolean isValid(T value);

  /** @return the target validator. */
  Validator<T> validator();

}
