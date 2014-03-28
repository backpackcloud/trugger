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

package org.atatec.trugger.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A set of predicates to use with <code>Method</code> objects.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public class MethodPredicates {

  private MethodPredicates() {
  }

  private static final Pattern TO_PATTERN = Pattern.compile("to[A-Z].*");
  private static final Pattern GET_PATTERN = Pattern.compile("get[A-Z].*");
  private static final Pattern SET_PATTERN = Pattern.compile("set[A-Z].*");
  private static final Pattern IS_PATTERN = Pattern.compile("is[A-Z].*");

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a getter
   * method.
   * <p>
   * The method <strong>may</strong> have a prefix "get" or "is", take no parameter and
   * return an object. If the method has the prefix "is", then it must return a boolean
   * value.
   */
  public static final Predicate<Method> getter() {
    return method -> {
      if (!Modifier.isPublic(method.getModifiers())) {
        return false;
      }
      String name = method.getName();
      Class<?> returnType = method.getReturnType();
      if ((method.getParameterTypes().length != 0) || Reflection.isStatic(method) ||
          (returnType == null || returnType.equals(void.class) ||
              returnType.equals(Void.class))) {
        return false;
      }
      if (TO_PATTERN.matcher(name).matches()) {
        return false;
      }
      if (name.startsWith("get")) {
        return GET_PATTERN.matcher(name).matches();
      } else if (name.startsWith("is")) {
        boolean returnBoolean = (Boolean.class.equals(returnType) ||
            boolean.class.equals(returnType));
        return returnBoolean && IS_PATTERN.matcher(name).matches();
      }
      return true;
    };
  }

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a
   * setter method.
   * <p>
   * The method must have the "set" prefix, take one parameter and return no
   * value (a void method).
   */
  public static final Predicate<Method> setter() {
    return method -> {
      if (!Modifier.isPublic(method.getModifiers())) {
        return false;
      }
      Class returnType = method.getReturnType();
      if ((method.getParameterTypes().length != 1) ||
          !(returnType == null || returnType.equals(void.class) ||
              returnType.equals(Void.class))) {
        return false;
      }
      return SET_PATTERN.matcher(method.getName()).matches();
    };
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a getter
   * method for the specified property name.
   */
  public static Predicate<Method> getterOf(String propertyName) {
    return getter().and(
        method -> Reflection.parsePropertyName(method).equals(propertyName));
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a setter
   * method for the specified property name.
   */
  public static Predicate<Method> setterOf(String propertyName) {
    return setter().and(
        method -> Reflection.parsePropertyName(method).equals(propertyName));
  }

  public static Predicate<Method> getterOf(Field field) {
    return getterOf(field.getName()).and(returns(field.getType()));
  }

  public static Predicate<Method> setterOf(Field field) {
    return setterOf(field.getName()).and(withParameters(field.getType()));
  }

  /**
   * Returns a predicate that evaluates to <code>true</code> if the parameter
   * types of a method equals the given types.
   *
   * @param parameterTypes the parameter types
   * @return a predicate to evaluate the parameter types.
   * @since 5.0
   */
  public static Predicate<Method> withParameters(Class... parameterTypes) {
    return method -> Arrays.equals(method.getParameterTypes(), parameterTypes);
  }

  /**
   * Returns a predicate that evaluates to <code>true</code> if a method takes
   * no parameter.
   *
   * @return a predicate to evaluate the method.
   * @since 5.0
   */
  public static Predicate<Method> withoutParameters() {
    return method -> method.getParameterTypes().length == 0;
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method
   * has the specified type as the return type.
   */
  public static Predicate<Method> returns(Class returnType) {
    return method -> method.getReturnType().equals(returnType);
  }

  /**
   * @return a predicate that returns <code>true</code> if the method has
   * annotations.
   */
  public static final Predicate<Method> annotated() {
    return method -> method.getDeclaredAnnotations().length > 0;
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element
   * is annotated with the specified Annotation.
   */
  public static Predicate<Method> annotatedWith(
      final Class<? extends Annotation> annotationType) {
    return method -> method.isAnnotationPresent(annotationType);
  }

}
