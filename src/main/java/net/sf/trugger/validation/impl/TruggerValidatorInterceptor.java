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
package net.sf.trugger.validation.impl;

import static net.sf.trugger.reflection.ReflectionPredicates.named;
import static net.sf.trugger.reflection.ReflectionPredicates.ofReturnType;
import static net.sf.trugger.reflection.ReflectionPredicates.withParameters;
import static net.sf.trugger.util.Utils.isTypeAccepted;
import static net.sf.trugger.util.Utils.resolveType;

import java.lang.reflect.Method;

import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.validation.ArgumentsInterceptor;
import net.sf.trugger.validation.Validator;

/**
 * This class handles the calls to the method {@link Validator#isValid(Object)}
 * and cancels the validation if the Validator is configured to prevent errors.
 *
 * @author Marcelo Varella Barca Guimarães
 */
class TruggerValidatorInterceptor extends ArgumentsInterceptor {

  private static final Predicate<Method> predicate =
      ofReturnType(boolean.class).and(named("isValid")).and(withParameters(Object.class));

  @Override
  protected Object intercept() throws Throwable {
    if (predicate.evaluate(method())) {
      Object value = arg(0);
      Class<?> genericType = Reflection.reflect().genericType("T").in(target);
      if (value != null) {
        boolean generic = !genericType.equals(Object.class);
        if (generic ? !genericType.isAssignableFrom(value.getClass()) : !isTypeAccepted(value.getClass(), resolveType(target))) {
          throw new IllegalArgumentException(String.format(
              "The type %s is not compatible with any type defined in the validator.", value.getClass()));
        }
      }
    }
    return super.intercept();
  }

  @Override
  protected Object onInvalidArgument(Object argument) {
    return true;
  }

}
