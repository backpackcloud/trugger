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

import static net.sf.trugger.util.Utils.isTypeAccepted;
import static net.sf.trugger.util.Utils.resolveType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import net.sf.trugger.factory.Factory;
import net.sf.trugger.interception.Interceptor;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.validation.impl.ValidatorContextImpl;

/**
 * An {@link Interceptor} that validates the method arguments based on their
 * annotations.
 * <p>
 * The annotations that can be used are the ones that defines {@link Validator}
 * objects and does not have dependencies for injection.
 *
 * @see Validator
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class ArgumentsInterceptor extends Interceptor {

  private final Factory<ValidatorContext, Validator> factory = Validation.newValidatorFactory();

  private final List<GenericTypeMapper> mappers = new LinkedList<GenericTypeMapper>();

  protected final ArgumentIndexSelector ifMethodMatches(final Predicate<Method> predicate) {
    final GenericTypeMapper mapper = new GenericTypeMapper();
    mapper.predicate = predicate;
    return new ArgumentIndexSelector() {

      public GenericTypeSelector useArgument(int index) {
        mapper.argumentIndex = index;
        return new GenericTypeSelector() {
          public void andCheckGenericType(String genericType) {
            mapper.genericParam = genericType;
            mappers.add(mapper);
          }
        };
      }

    };
  }

  @Override
  protected Object intercept() throws Throwable {
    Method method = method();
    for (GenericTypeMapper mapper : mappers) {
      if (mapper.predicate.evaluate(method())) {
        Object value = arg(mapper.argumentIndex);
        Class<?> genericType = Reflection.reflect().genericType(mapper.genericParam).in(target);
        if (value != null) {
          boolean generic = !genericType.equals(Object.class);
          if (generic ? !genericType.isAssignableFrom(value.getClass()) : !isTypeAccepted(value.getClass(), resolveType(target))) {
            throw new IllegalArgumentException(String.format(
                "The type %s is not compatible with any type defined in the validator.", value.getClass()));
          }
        }
      }
    }
    try {
      test(method);
      test(getTargetMethod());
    } catch (InvalidArgumentException e) {
      return onInvalidArgument(e.getArgument());
    }
    return invokeMethod();
  }

  /**
   * Validates the arguments passed based on the annotations in the given
   * method.
   *
   * @param method
   *          the method to get the annotations.
   */
  protected final void test(Method method) {
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    Object[] args = args();
    for (int i = 0 ; i < parameterAnnotations.length ; i++) {
      Annotation[] annotations = parameterAnnotations[i];
      for (Annotation annotation : annotations) {
        ValidatorContext context = new ValidatorContextImpl(annotation);
        if (factory.canCreate(context)) {
          Validator validator = factory.create(context);
          if (!validator.isValid(args[i])) {
            throw new InvalidArgumentException(args[i]);
          }
        }
      }
    }
  }

  /**
   * This method is called if an argument is invalid.
   *
   * @param argument
   *          the invalid argument.
   * @return the return that the method must give. This implementations throws
   *         an IllegalArgumentException.
   */
  protected Object onInvalidArgument(Object argument) {
    throw new IllegalArgumentException("Invalid argument passed: " + String.valueOf(argument));
  }

  private static class GenericTypeMapper {
    Predicate<Method> predicate;
    String genericParam;
    int argumentIndex;
  }

  public interface ArgumentIndexSelector {

    GenericTypeSelector useArgument(int index);

  }

  public interface GenericTypeSelector {

    void andCheckGenericType(String genericType);

  }

}

/**
 * Thrown to indicate that a method has been passed an invalid argument.
 *
 * @author Marcelo Varella Barca Guimarães
 */
class InvalidArgumentException extends RuntimeException {

  private static final long serialVersionUID = 8241748932228458376L;

  private Object argument;

  /**
   * @param argument
   *          the invalid argument.
   */
  public InvalidArgumentException(Object argument) {
    this.argument = argument;
  }

  /**
   * @return the invalid argument.
   */
  public Object getArgument() {
    return argument;
  }

}
