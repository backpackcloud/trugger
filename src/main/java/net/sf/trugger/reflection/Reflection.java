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
package net.sf.trugger.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.trugger.Invoker;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.loader.ImplementationLoader;

/**
 * An utility class for help the use of Reflection.
 * <p>
 * This class also uses a {@link ReflectionFactory} for some operations.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class Reflection {

  private static final ReflectionFactory factory;

  private static final Map<Class<?>, Class<?>> wrappers;

  private Reflection() {}

  static {
    factory = ImplementationLoader.getInstance().get(ReflectionFactory.class);
    wrappers = Collections.unmodifiableMap(new HashMap<Class<?>, Class<?>>() {

      {
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(char.class, Character.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(boolean.class, Boolean.class);
      }
    });
  }

  /**
   * Returns the wrapper class for the given {@link Class#isPrimitive()
   * primitive class}.
   *
   * @return the wrapper class for the given primitive class.
   */
  public static Class<?> wrapperFor(Class<?> primitiveClass) {
    return wrappers.get(primitiveClass);
  }

  /**
   * @return <code>true</code> if the specified member has the
   *         <code>public</code> modifier
   */
  public static boolean isPublic(Member member) {
    return Modifier.isPublic(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the
   *         <code>private</code> modifier
   */
  public static boolean isPrivate(Member member) {
    return Modifier.isPrivate(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the
   *         <code>final</code> modifier
   */
  public static boolean isFinal(Member member) {
    return Modifier.isFinal(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the
   *         <code>static</code> modifier
   */
  public static boolean isStatic(Member member) {
    return Modifier.isStatic(member.getModifiers());
  }

  /**
   * Sets the accessible flag of the given objects to <code>true</code> using a
   * {@link PrivilegedAction}.
   *
   * @param objs
   *          the objects for making accessible.
   */
  public static void setAccessible(final AccessibleObject... objs) {
    AccessController.doPrivileged(new PrivilegedAction<Void>() {

      public Void run() {
        AccessibleObject.setAccessible(objs, true);
        return null;
      }
    });
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link Reflector}
   * instance.
   *
   * @return a component for reflecting objects in a target.
   */
  public static Reflector reflect() {
    return factory.createReflector();
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link MethodInvoker}
   * instance.
   *
   * @param method
   *          a method to invoke.
   * @return a component for invoking the given method.
   */
  public static MethodInvoker invoke(Method method) {
    return factory.createInvoker(method);
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a
   * {@link ConstructorInvoker} instance.
   *
   * @param constructor
   *          the constructor to invoke.
   * @return a component for invoking the given constructor.
   */
  public static ConstructorInvoker invoke(Constructor<?> constructor) {
    return factory.createInvoker(constructor);
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link FieldHandler}
   * instance.
   *
   * @param field
   *          the field to handle.
   * @return a component for handling the given field.
   */
  public static FieldHandler handle(Field field) {
    return factory.createHandler(field);
  }

  /**
   * Handles a collection of fields.
   * <p>
   * Is extremely important that <strong>all</strong> fields have the same type,
   * but the access way (static or non-static) does not matter.
   * <p>
   * For getting the values, the return type will be a {@link Collection} of
   * {@link Object objects} containing the values based on the iteration order.
   *
   * @param fields
   *          the fields for handling.
   * @return a component for handling the fields.
   */
  public static FieldHandler handle(final Collection<Field> fields) {
    return new FieldHandler() {

      private Object target;

      public ValueHandler in(Object source) {
        this.target = source;
        return this;
      }

      public <E> E value() {
        Collection result = new ArrayList(fields.size());
        for (Field field : fields) {
          if (isStatic(field)) {
            result.add(handle(field).value());
          } else {
            result.add(handle(field).in(target).value());
          }
        }
        return (E) result;
      }

      public void value(Object value) {
        for (Field field : fields) {
          if (isStatic(field)) {
            handle(field).value(value);
          } else {
            handle(field).in(target).value(value);
          }
        }
      }
    };
  }

  /**
   * Invokes a collection of methods that have the same parameters. The access
   * way (static or non-static) does not matter.
   * <p>
   * The return type will be a {@link Collection} of {@link Object objects}
   * containing the values based on the iteration order.
   *
   * @param methods
   *          the methods to invoke.
   * @return a component for invoking the methods.
   */
  public static MethodInvoker invoke(final Collection<Method> methods) {
    return new MethodInvoker() {

      private Object target;

      public Invoker in(Object instance) {
        this.target = instance;
        return this;
      }

      public <E> E withArgs(Object... args) {
        Collection results = new ArrayList();
        for (Method method : methods) {
          if (isStatic(method)) {
            results.add(invoke(method).withArgs(args));
          } else {
            results.add(invoke(method).in(target).withArgs(args));
          }
        }
        return (E) results;
      }

      public <E> E withoutArgs() {
        return (E) withArgs();
      };
    };
  }
}
