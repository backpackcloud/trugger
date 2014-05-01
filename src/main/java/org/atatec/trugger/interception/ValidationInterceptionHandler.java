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

package org.atatec.trugger.interception;

import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.impl.ValidatorFactoryImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

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

  /**
   * Creates a new interceptor that invokes the target method if the arguments
   * are valid and throws a <code>IllegalArgumentException</code> in case of
   * any invalid argument.
   * <p>
   * Validators are created by the {@link ValidatorFactoryImpl default}
   * validator factory.
   */
  public ValidationInterceptionHandler() {
    this(new ValidatorFactoryImpl());
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
    this.validHandler = (context) -> context.invoke();
    this.inValidHandler = (context) -> {
      throw new IllegalArgumentException();
    };
  }

  private ValidationInterceptionHandler(ValidatorFactory factory,
                                        InterceptionHandler validHandler,
                                        InterceptionHandler inValidHandler) {
    this.factory = factory;
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
    return new ValidationInterceptionHandler(factory, handler, inValidHandler);
  }

  /**
   * Defines a handler to use in case of an argument is invalid.
   *
   * @param handler the handler to use
   * @return a new interceptor that uses the given handler.
   */
  public ValidationInterceptionHandler onInvalid(InterceptionHandler handler) {
    return new ValidationInterceptionHandler(factory, validHandler, handler);
  }

  @Override
  public Object intercept(InterceptionContext context) throws Throwable {
    boolean valid = true;
    if (context.target() != null) {
      valid &= isValid(context.targetMethod(), context.args());
    }
    valid &= isValid(context.method(), context.args());
    return valid ? validHandler.intercept(context) :
        inValidHandler.intercept(context);
  }

  /**
   * Checks if the arguments are valid using the annotations defined in the
   * parameters.
   *
   * @param executable the executable to extract the parameters
   * @param args       the arguments passed
   * @return <code>true</code> if all arguments are valid or none of the
   * parameters has constraints.
   */
  private boolean isValid(Executable executable, Object[] args) {
    int i = 0;
    Validator validator;
    for (Parameter parameter : executable.getParameters()) {
      for (Annotation annotation : parameter.getAnnotations()) {
        validator = factory.create(annotation);
        if (validator != null && !validator.isValid(args[i])) {
          return false;
        }
      }
      i++;
    }
    return true;
  }

}
