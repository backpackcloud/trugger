/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation;

import net.sf.trugger.message.MessageCreator;

/**
 * Interface for creating validation engines.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public interface ValidationFactory {

  /**
   * @return a binder for injecting dependencies.
   * @since 2.4
   */
  ValidatorBinder createValidatorBinder();

  /**
   * @return a factory for creating {@link Validator} objects.
   */
  ValidatorFactory createValidatorFactory(ValidatorBinder validatorBinder);

  /**
   * Creates a new engine for validating a target.
   *
   * @param validatorFactory
   *          the factory to create the validators.
   * @param messageCreator
   *          the component to create the messages.
   * @return the created engine.
   */
  ValidationEngine createValidationEngine(ValidatorFactory validatorFactory, MessageCreator messageCreator);

}
