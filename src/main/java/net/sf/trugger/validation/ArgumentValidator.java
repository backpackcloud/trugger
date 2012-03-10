/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import net.sf.trugger.factory.Factory;
import net.sf.trugger.validation.impl.ValidatorContextImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 3.0
  */
public class ArgumentValidator {

  private final Factory<ValidatorContext, ValidatorInvoker> factory = Validation.newValidatorFactory();
  private Method method;

  public ArgumentValidator(Method method) {
    this.method = method;
  }

  public boolean areArgumentsValid(Object... args) {
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for(int i = 0 ; i < args.length ; i++) {
      Annotation[] annotations = parameterAnnotations[i];
      Object value = args[i];
      if(isArgumentInvalid(value, annotations)) {
        return false;
      }
    }
    return true;
  }

  private boolean isArgumentInvalid(Object value, Annotation[] annotations) {
    ValidatorContext context;
    for (Annotation annotation : annotations) {
      context = new ValidatorContextImpl(annotation);
      if (factory.canCreate(context)) {
        ValidatorInvoker validator = factory.create(context);
        if (!validator.isValid(value)) {
          return true;
        }
      }
    }
    return false;
  }

}