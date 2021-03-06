/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.backpackcloud.trugger.reflection;

import com.backpackcloud.trugger.reflection.impl.TruggerReflectionFactory;
import com.backpackcloud.trugger.util.ClassIterator;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An utility class for help the use of Reflection.
 * <p>
 * This class also uses a {@link ReflectionFactory} for some operations.
 *
 * @author Marcelo Guimaraes
 */
public final class Reflection {

  private static final ReflectionFactory factory;

  private static final Map<Class<?>, Class<?>> wrappers;

  private Reflection() {
  }

  static {
    factory = new TruggerReflectionFactory();
    wrappers = new HashMap<>();
    wrappers.put(byte.class, Byte.class);
    wrappers.put(short.class, Short.class);
    wrappers.put(int.class, Integer.class);
    wrappers.put(long.class, Long.class);
    wrappers.put(char.class, Character.class);
    wrappers.put(float.class, Float.class);
    wrappers.put(double.class, Double.class);
    wrappers.put(boolean.class, Boolean.class);
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
    return Character.toLowerCase(name.charAt(i)) + name.substring(i + 1);
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
   * @return a list containing the hierarchy of the given target.
   * @see ClassIterator
   * @since 6.0
   */
  public static List<Class> hierarchyOf(Object target) {
    List<Class> result = new ArrayList<>();
    new ClassIterator(target).forEachRemaining(result::add);
    return result;
  }

  /**
   * Returns a consumer for setting a value to a ReflectedField
   *
   * @param newValue the value to set
   * @return a consumer for setting a value to a ReflectedField
   * @since 7.0
   */
  public static Consumer<ReflectedField> setValue(Object newValue) {
    return reflectedField -> reflectedField.setValue(newValue);
  }

  /**
   * Returns a consumer for invoking a ReflectedMethod
   *
   * @param args the arguments to pass
   * @return a consumer for invoking a ReflectedMethod
   * @since 7.0
   */
  public static Consumer<ReflectedMethod> invoke(Object... args) {
    return reflectedMethod -> reflectedMethod.invoke(args);
  }

}
