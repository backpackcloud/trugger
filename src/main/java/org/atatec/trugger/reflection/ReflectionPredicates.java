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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * An utility class for helping the use of {@link Predicate} object that involves
 * Reflection.
 *
 * @author Marcelo Guimarães
 */
public class ReflectionPredicates {

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
  public static final Predicate<Method> GETTER = method -> {
    if (!Modifier.isPublic(method.getModifiers())) {
      return false;
    }
    String name = method.getName();
    Class<?> returnType = method.getReturnType();
    if ((method.getParameterTypes().length != 0) || Reflection.isStatic(method) ||
      (returnType == null || returnType.equals(void.class) || returnType.equals(Void.class))) {
      return false;
    }
    if (TO_PATTERN.matcher(name).matches()) {
      return false;
    }
    if (name.startsWith("get")) {
      return GET_PATTERN.matcher(name).matches();
    } else if (name.startsWith("is")) {
      boolean returnBoolean = (Boolean.class.equals(returnType) || boolean.class.equals(returnType));
      return returnBoolean && IS_PATTERN.matcher(name).matches();
    }
    return true;
  };

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a setter
   * method.
   * <p>
   * The method must have the "set" prefix, take one parameter and return no value (a void
   * method).
   */
  public static final Predicate<Method> SETTER = method -> {
    if (!Modifier.isPublic(method.getModifiers())) {
      return false;
    }
    Class returnType = method.getReturnType();
    if ((method.getParameterTypes().length != 1) ||
      !(returnType == null || returnType.equals(void.class) || returnType.equals(Void.class))) {
      return false;
    }
    return SET_PATTERN.matcher(method.getName()).matches();
  };

  /**
   * @return a predicate that returns <code>true</code> if a method is a getter method for
   * the specified property name.
   */
  public static Predicate<Method> getterOf(String propertyName) {
    return GETTER.and(method -> Reflection.parsePropertyName(method).equals(propertyName));
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a setter method for
   * the specified property name.
   */
  public static Predicate<Method> setterOf(String propertyName) {
    return SETTER.and(method -> Reflection.parsePropertyName(method).equals(propertyName));
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>interface</i> and is not
   * an <i>annotation</i>.
   */
  public static final Predicate<Class> INTERFACE =
    ClassPredicates.declare(Modifier.INTERFACE).and(notAssignableTo(Annotation.class));
  /**
   * The negation of the {@link #INTERFACE} predicate.
   */
  public static final Predicate<Class> NOT_INTERFACE = INTERFACE.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>enum</i>.
   */
  public static final Predicate<Class> ENUM = element -> element.isEnum();

  /**
   * The negation of the {@link #ENUM} predicate.
   */
  public static final Predicate<Class> NOT_ENUM = ENUM.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>annotation</i>.
   */
  public static final Predicate<Class> ANNOTATION =
    ClassPredicates.declare(Modifier.INTERFACE).and(assignableTo(Annotation.class));
  /**
   * The negation of the {@link #ANNOTATION} predicate.
   */
  public static final Predicate<Class> NOT_ANNOTATION = ANNOTATION.negate();
  /**
   * Predicate that returns <code>true</code> if a class is not an <i>interface</i> and is
   * not an <i>enum</i>.
   */
  public static final Predicate<Class> CLASS = NOT_INTERFACE.and(NOT_ENUM).and(NOT_ANNOTATION);

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element is
   * annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> Predicate<T> isAnnotatedWith(
    final Class<? extends Annotation> annotationType) {
    return element -> element.isAnnotationPresent(annotationType);
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated element is
   * annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> Predicate<T> isNotAnnotatedWith(
    final Class<? extends Annotation> annotationType) {
    return ReflectionPredicates.<T>isAnnotatedWith(annotationType).negate();
  }

  /**
   * A predicate that returns <code>true</code> if the element has that annotations.
   */
  public static final Predicate<AnnotatedElement> ANNOTATED =
    element -> element.getDeclaredAnnotations().length > 0;

  /**
   * A predicate that returns <code>false</code> if the element has that annotation.
   */
  public static final Predicate<AnnotatedElement> NOT_ANNOTATED = ANNOTATED.negate();

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is
   * assignable from the evaluated element.
   */
  public static Predicate<Class> assignableTo(Class clazz) {
    return element -> clazz.isAssignableFrom(element);
  }

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is not
   * assignable from the evaluated element.
   */
  public static Predicate<Class> notAssignableTo(final Class clazz) {
    return assignableTo(clazz).negate();
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has a
   * name that with the given one.
   */
  public static <T extends Member> Predicate<T> named(final String name) {
    return element -> element.getName().equals(name);
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has the
   * specified modifiers.
   */
  public static <T extends Member> Predicate<T> declare(int... modifiers) {
    return element -> {
      int elModifiers = element.getModifiers();
      for (int mod : modifiers) {
        if ((elModifiers & mod) != 0) {
          return true;
        }
      }
      return false;
    };
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated element does not
   * have the specified modifiers.
   */
  public static <T extends Member> Predicate<T> dontDeclare(int... modifiers) {
    return element -> {
      int elModifiers = element.getModifiers();
      for (int mod : modifiers) {
        if ((elModifiers & mod) != 0) {
          return false;
        }
      }
      return true;
    };
  }

}
