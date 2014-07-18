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

package tools.devnull.trugger.interception;

import tools.devnull.trugger.validation.ArgumentsValidator;
import tools.devnull.trugger.validation.ValidatorFactory;
import tools.devnull.trugger.validation.impl.TruggerValidatorFactory;

/**
 * An interception handler that validates method arguments using a
 * {@link ValidatorFactory}.
 *
 * @since 5.1
 */
public final class ValidationInterceptionHandler implements InterceptionHandler {

  private final ValidatorFactory factory;
  private final InterceptionHandler validHandler;
  private final InterceptionHandler inValidHandler;
  private final ArgumentsValidator argumentsValidator;

  /**
   * Creates a new interceptor that invokes the target method if the arguments
   * are valid and throws a <code>IllegalArgumentException</code> in case of
   * any invalid argument.
   * <p>
   * Validators are created by the {@link tools.devnull.trugger.validation.impl.TruggerValidatorFactory default}
   * validator factory.
   */
  public ValidationInterceptionHandler() {
    this(new TruggerValidatorFactory());
  }

  /**
   * Creates a new interceptor that invokes the target method if the arguments
   * are valid and throws a <code>IllegalArgumentException</code> in case of
   * any invalid argument.
   *
   * @param factory the factory to instantiate validators
   */
  public ValidationInterceptionHandler(ValidatorFactory factory) {
    this.factory = factory;
    this.argumentsValidator = new ArgumentsValidator(factory);
    this.validHandler = (context) -> context.invoke();
    this.inValidHandler = (context) -> {
      throw new IllegalArgumentException();
    };
  }

  private ValidationInterceptionHandler(ValidatorFactory factory,
                                        ArgumentsValidator argumentsValidator,
                                        InterceptionHandler validHandler,
                                        InterceptionHandler inValidHandler) {
    this.factory = factory;
    this.argumentsValidator = argumentsValidator;
    this.validHandler = validHandler;
    this.inValidHandler = inValidHandler;
  }

  /**
   * Defines a handler to use in case of all arguments are valid.
   *
   * @param handler the handler to use
   * @return a new interceptor that uses the given handler.
   */
  public ValidationInterceptionHandler onValid(InterceptionHandler handler) {
    return new ValidationInterceptionHandler(
        factory, argumentsValidator, handler, inValidHandler);
  }

  /**
   * Defines a handler to use in case of an argument is invalid.
   *
   * @param handler the handler to use
   * @return a new interceptor that uses the given handler.
   */
  public ValidationInterceptionHandler onInvalid(InterceptionHandler handler) {
    return new ValidationInterceptionHandler(
        factory, argumentsValidator, validHandler, handler);
  }

  @Override
  public Object intercept(InterceptionContext context) throws Throwable {
    boolean valid = true;
    if (context.target() != null) {
      valid &= argumentsValidator.isValid(context.targetMethod(), context.args());
    }
    valid &= argumentsValidator.isValid(context.method(), context.args());
    return valid ? validHandler.intercept(context) :
        inValidHandler.intercept(context);
  }

}
