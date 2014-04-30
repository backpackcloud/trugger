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

package org.atatec.trugger.validation.impl;

import org.atatec.trugger.interception.InterceptionContext;
import org.atatec.trugger.interception.InterceptionHandler;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 *
 */
public class ValidatorInterceptor implements InterceptionHandler {

  private final ValidatorFactory factory;

  public ValidatorInterceptor(ValidatorFactory factory) {
    this.factory = factory;
  }

  @Override
  public Object intercept(InterceptionContext context) throws Throwable {
    int i = 0;
    Validator validator;
    for (Parameter parameter : context.targetMethod().getParameters()) {
      for (Annotation annotation : parameter.getAnnotations()) {
        validator = factory.create(annotation);
        if (validator != null && !validator.isValid(context.args()[i])) {
          return true;
        }
      }
      i++;
    }
    return context.invoke();
  }

}
