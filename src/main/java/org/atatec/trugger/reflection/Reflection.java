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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.Invoker;
import org.atatec.trugger.Result;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.reflection.impl.MethodSelectorInvoker;
import org.atatec.trugger.selector.*;
import org.atatec.trugger.util.ClassIterator;
import org.atatec.trugger.util.Utils;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Predicate;

/**
 * An utility class for help the use of Reflection.
 * <p>
 * This class also uses a {@link ReflectionFactory} for some operations.
 *
 * @author Marcelo Guimarães
 */
public final class Reflection {

  private static final ReflectionFactory factory;

  private static final Map<Class<?>, Class<?>> wrappers;

  private Reflection() {
  }

  static {
    factory = ImplementationLoader.get(ReflectionFactory.class);
    wrappers = new HashMap<Class<?>, Class<?>>() {{
      put(byte.class, Byte.class);
      put(short.class, Short.class);
      put(int.class, Integer.class);
      put(long.class, Long.class);
      put(char.class, Character.class);
      put(float.class, Float.class);
      put(double.class, Double.class);
      put(boolean.class, Boolean.class);
    }};
  }

  /**
   * Returns the wrapper class for the given {@link Class#isPrimitive() primitive class}.
   *
   * @return the wrapper class for the given primitive class.
   */
  public static Class<?> wrapperFor(Class<?> primitiveClass) {
    return wrappers.get(primitiveClass);
  }

  /**
   * @return <code>true</code> if the specified member has the <code>public</code>
   * modifier
   */
  public static boolean isPublic(Member member) {
    return Modifier.isPublic(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>private</code>
   * modifier
   */
  public static boolean isPrivate(Member member) {
    return Modifier.isPrivate(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>final</code>
   * modifier
   */
  public static boolean isFinal(Member member) {
    return Modifier.isFinal(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>static</code>
   * modifier
   */
  public static boolean isStatic(Member member) {
    return Modifier.isStatic(member.getModifiers());
  }

  /**
   * Sets the accessible flag of the given objects to <code>true</code> using a {@link
   * PrivilegedAction}.
   *
   * @param objs the objects for making accessible.
   */
  public static void setAccessible(AccessibleObject... objs) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      AccessibleObject.setAccessible(objs, true);
      return null;
    });
  }

  /**
   * Parses the method name to return a property name if it is a getter or a setter.
   *
   * @param method the method to evaluate
   * @return a property name
   * @since 4.1
   */
  public static String parsePropertyName(Method method) {
    String name = method.getName();
    int i = 0;
    if (name.startsWith("get") || name.startsWith("set")) {
      i = 3;
    } else if (name.startsWith("is")) {
      i = 2;
    }
    return i > 0 ? Character.toLowerCase(name.charAt(i)) + name.substring(i + 1) : name;
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link Reflector} instance.
   *
   * @return a component for reflecting objects in a target.
   */
  public static Reflector reflect() {
    return factory.createReflector();
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link MethodInvoker} instance.
   *
   * @param method a method to invoke.
   * @return a component for invoking the given method.
   */
  public static MethodInvoker invoke(Method method) {
    return factory.createInvoker(method);
  }

  /**
   * The same as <code>reflect().constructor()</code>
   *
   * @since 2.8
   */
  public static ConstructorSelector constructor() {
    return reflect().constructor();
  }

  /**
   * The same as <code>reflect().constructors()</code>
   *
   * @since 2.8
   */
  public static ConstructorsSelector constructors() {
    return reflect().constructors();
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link ConstructorInvoker}
   * instance.
   *
   * @param constructor the constructor to invoke.
   * @return a component for invoking the given constructor.
   */
  public static ConstructorInvoker invoke(Constructor<?> constructor) {
    return factory.createInvoker(constructor);
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link FieldHandler} instance.
   *
   * @param field the field to handle.
   * @return a component for handling the given field.
   */
  public static FieldHandler handle(Field field) {
    return factory.createHandler(field);
  }

  /**
   * The same as <code>reflect().field(String)</code>
   *
   * @since 2.8
   */
  public static FieldSelector field(String name) {
    return reflect().field(name);
  }

  /**
   * The same as <code>reflect().fields()</code>
   *
   * @since 2.8
   */
  public static FieldsSelector fields() {
    return reflect().fields();
  }

  /**
   * Handles the field selected by the given selector.
   *
   * @param selector the selector for getting the field.
   * @return the handler.
   * @since 2.8
   */
  public static Result<ValueHandler, Object> handle(final FieldSelector selector) {
    return new FieldHandler() {

      private Object target;

      @Override
      public <E> E value() throws HandlingException {
        Field field = selector.in(target);
        return handle(field).in(target).value();
      }

      @Override
      public void value(Object value) throws HandlingException {
        Field field = selector.in(target);
        handle(field).in(target).value(value);
      }

      @Override
      public ValueHandler in(Object source) {
        this.target = source;
        return this;
      }
    };
  }

  /**
   * The same as <code>reflect().method(String)</code>
   *
   * @since 2.8
   */
  public static MethodSelector method(String name) {
    return reflect().method(name);
  }

  /**
   * The same as <code>reflect().methods()</code>
   *
   * @since 2.8
   */
  public static MethodsSelector methods() {
    return reflect().methods();
  }

  /**
   * Invokes the method selected by the given selector.
   *
   * @param selector the selector for getting the method.
   * @return the invoker
   * @since 2.8
   */
  public static Result<Invoker, Object> invoke(MethodSelector selector) {
    return new MethodSelectorInvoker(selector);
  }

  /**
   * Creates a new instance of the given type by locating the proper constructor based on
   * the given arguments.
   *
   * @param type                 the instance type
   * @param constructorArguments the arguments to call the constructor.
   * @return a new instance of the give type
   * @since 2.5
   */
  public static <E> E newInstanceOf(final Class<E> type, final Object... constructorArguments) {

    if (constructorArguments.length == 0) {
      Constructor<?> constructor = reflect().constructor().withoutParameters().in(type);
      return invoke(constructor).withoutArgs();
    }
    final Class<?>[] parameters = new Class[constructorArguments.length];
    for (int i = 0; i < constructorArguments.length; i++) {
      Object parameter = constructorArguments[i];
      if (parameter != null) {
        parameters[i] = Utils.resolveType(parameter);
      }
    }
    Constructor<?> foundConstructor = reflect().constructor().withParameters(parameters).in(type);
    if (foundConstructor != null) {
      return invoke(foundConstructor).withArgs(constructorArguments);
    }
    Set<Constructor<?>> constructors = reflect().constructors().in(type);
    Predicate<Constructor<?>> matchingConstructor = constructor -> {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length != parameters.length) {
        return false;
      }
      for (int i = 0; i < parameters.length; i++) {
        Class<?> param = parameters[i];
        if (param != null && !Utils.areAssignable(parameterTypes[i], param)) {
          return false;
        }
      }
      return true;
    };
    Optional<Constructor<?>> constructor = constructors.stream()
        .filter(matchingConstructor)
        .findAny();
    if (constructor.isPresent()) {
      return invoke(constructor.get()).withArgs(constructorArguments);
    }
    throw new ReflectionException("No constructor found");
  }

  /**
   * @return an iterable Class hierarchy for use in "foreach" loops.
   * @see ClassIterator
   * @since 4.0
   */
  public static Iterable<Class> hierarchyOf(final Object target) {
    return () -> new ClassIterator(target);
  }

}

