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

package org.atatec.trugger.validation;

import org.atatec.trugger.factory.Factory;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.validation.impl.ValidatorContextImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.atatec.trugger.util.Utils.isTypeAccepted;
import static org.atatec.trugger.util.Utils.resolveType;

/**
 * An utility class to validate arguments passed to a method using generic types and
 * {@link Validator} annotations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 3.0
 */
public class ArgumentValidator {

  private final Factory<ValidatorContext, ValidatorInvoker> factory = Validation.newValidatorFactory();
  private Method method;
  private String[] genericTypes;

  /**
   * Creates a new validator for a method.
   *
   * @param method       the method to obtain information about annotations and generic
   *                     types
   * @param genericTypes the generic string of each argument
   */
  public ArgumentValidator(Method method, String... genericTypes) {
    this.method = method;
    this.genericTypes = genericTypes;
  }

  /**
   * Checks if the given arguments are valid using the method {@link Validator}
   * annotations.
   */
  public boolean areArgumentsValid(Object... args) {
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for (int i = 0; i < args.length; i++) {
      Annotation[] annotations = parameterAnnotations[i];
      Object value = args[i];
      if (isArgumentInvalid(value, annotations)) {
        return false;
      }
    }
    return true;
  }

  /** Checks if the given arguments matches the defined generic types. */
  public boolean argumentsMatchesGenericTypes(Object... args) {
    for (int i = 0; i < args.length; i++) {
      Object value = args[i];
      Class<?> genericType = Reflection.reflect().genericType(genericTypes[i]).in(method.getDeclaringClass());
      if (value != null) {
        boolean generic = !genericType.equals(Object.class);
        if (generic ? !genericType.isAssignableFrom(value.getClass()) : !isTypeAccepted(value.getClass(),
          resolveType(method.getDeclaringClass()))) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isArgumentInvalid(Object value, Annotation[] annotations) {
    ValidatorContext context;
    for (Annotation annotation : annotations) {
      context = new ValidatorContextImpl().annotation(annotation);
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
