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

package org.atatec.trugger.validation.impl;

import org.atatec.trugger.factory.Factory;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.validation.ArgumentValidator;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.lang.reflect.Method;

/**
 * A base invoker class that checks if the argument can be validated.
 *
 * @author Marcelo Guimarães
 * @since 3.0
 */
public class TruggerValidatorInvoker<T> implements ValidatorInvoker<T> {

  private final Factory<ValidatorContext, ValidatorInvoker> factory = Validation.newValidatorFactory();
  private final Validator validator;
  private Class genericType;
  private ArgumentValidator argValidator;

  public TruggerValidatorInvoker(Validator<T> validator) {
    this.validator = validator;
    this.genericType = Reflection.reflect().genericType("T").in(validator);
    Method method = Reflection.reflect()
      .method("isValid")
      .withParameters(genericType)
      .returning(boolean.class)
      .in(validator);
    this.argValidator = new ArgumentValidator(method, "T");
  }

  @Override
  public boolean isValid(T value) {
    checkArgumentType(value);
    return !argValidator.areArgumentsValid(value) || validator.isValid(value);
  }

  private void checkArgumentType(T value) {
    if (!argValidator.argumentsMatchesGenericTypes(value)) {
      throw new IllegalArgumentException(String.format(
        "The type %s is not compatible with any type defined in the validator.", value.getClass()));
    }
  }

  public Validator<T> validator() {
    return validator;
  }

}
