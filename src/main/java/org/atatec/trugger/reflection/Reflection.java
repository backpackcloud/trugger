/*
 * Copyright 2009-2012 Marcelo Guimarães
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
import org.atatec.trugger.exception.ExceptionHandler;
import org.atatec.trugger.exception.ExceptionHandlers;
import org.atatec.trugger.iteration.Find;
import org.atatec.trugger.iteration.NonUniqueMatchException;
import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.selector.ConstructorSelector;
import org.atatec.trugger.selector.ConstructorsSelector;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.selector.FieldsSelector;
import org.atatec.trugger.selector.MethodSelector;
import org.atatec.trugger.selector.MethodsSelector;
import org.atatec.trugger.util.ClassIterator;
import org.atatec.trugger.util.Utils;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An utility class for help the use of Reflection.
 * <p/>
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
   *         modifier
   */
  public static boolean isPublic(Member member) {
    return Modifier.isPublic(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>private</code>
   *         modifier
   */
  public static boolean isPrivate(Member member) {
    return Modifier.isPrivate(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>final</code>
   *         modifier
   */
  public static boolean isFinal(Member member) {
    return Modifier.isFinal(member.getModifiers());
  }

  /**
   * @return <code>true</code> if the specified member has the <code>static</code>
   *         modifier
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
  public static void setAccessible(final AccessibleObject... objs) {
    AccessController.doPrivileged(new PrivilegedAction<Void>() {

      public Void run() {
        AccessibleObject.setAccessible(objs, true);
        return null;
      }
    });
  }

  /**
   * Parses the method name to return a property name if it is a getter or a setter.
   *
   * @param method the method to evaluate
   *
   * @return a property name
   *
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
   * Uses the {@link ReflectionFactory} for creating a {@link Reflector} instance and adds
   * the given predicate to the filter.
   *
   * @return a component for reflecting objects in a target.
   * @since 4.1
   */
  public static Reflector reflect(Predicate predicate) {
    return factory.createReflector(predicate);
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link MethodInvoker} instance.
   *
   * @param method a method to invoke.
   *
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
   *
   * @return a component for invoking the given constructor.
   */
  public static ConstructorInvoker invoke(Constructor<?> constructor) {
    return factory.createInvoker(constructor);
  }

  /**
   * Uses the {@link ReflectionFactory} for creating a {@link FieldHandler} instance.
   *
   * @param field the field to handle.
   *
   * @return a component for handling the given field.
   */
  public static FieldHandler handle(Field field) {
    return factory.createHandler(field);
  }

  /**
   * The same as <code>reflect().field()</code>
   *
   * @since 2.8
   */
  public static FieldSelector field() {
    return reflect().field();
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
   *
   * @return the handler.
   *
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
   * Handles the field selected by the given selector.
   *
   * @param selector the selector for getting the field.
   *
   * @return the handler.
   *
   * @since 2.8
   */
  public static Result<ValueHandler, Object> handle(final FieldsSelector selector) {
    return new FieldHandler() {

      private Object target;

      @Override
      public <E> E value() throws HandlingException {
        Set<Field> fields = selector.in(target);
        return handle(fields).in(target).value();
      }

      @Override
      public void value(Object value) throws HandlingException {
        Set<Field> fields = selector.in(target);
        handle(fields).in(target).value(value);
      }

      @Override
      public ValueHandler in(Object source) {
        this.target = source;
        return this;
      }
    };
  }

  /**
   * Handles a collection of fields.
   * <p/>
   * Is extremely important that <strong>all</strong> fields have the same type, but the
   * access way (static or non-static) does not matter.
   * <p/>
   * For getting the values, the return type will be a {@link Collection} of {@link Object
   * objects} containing the values based on the iteration order.
   *
   * @param fields the fields for handling.
   *
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
   * The same as <code>reflect().method()</code>
   *
   * @since 2.8
   */
  public static MethodSelector method() {
    return reflect().method();
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
   *
   * @return the invoker
   *
   * @since 2.8
   */
  public static Result<Invoker, Object> invoke(final MethodSelector selector) {
    return new MethodInvoker() {

      private Object target;

      private ExceptionHandler handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;


      @Override
      public Invoker in(Object instance) {
        this.target = instance;
        return this;
      }

      @Override
      public <E> E withArgs(Object... args) {
        Method method = selector.in(target);
        return invoke(method).in(target).handlingExceptionsWith(handler).withArgs(args);
      }

      @Override
      public <E> E withoutArgs() {
        return withArgs();
      }

      @Override
      public Invoker handlingExceptionsWith(ExceptionHandler handler) {
        this.handler = handler;
        return this;
      }

    };
  }

  /**
   * Invokes the methods selected by the given selector.
   *
   * @param selector the selector for getting the methods.
   *
   * @return the invoker
   *
   * @since 2.8
   */
  public static Result<Invoker, Object> invoke(final MethodsSelector selector) {
    return new MethodInvoker() {

      private Object target;

      private ExceptionHandler handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;

      @Override
      public Invoker in(Object instance) {
        this.target = instance;
        return this;
      }

      @Override
      public <E> E withArgs(Object... args) {
        Set<Method> methods = selector.in(target);
        return invoke(methods).in(target).handlingExceptionsWith(handler).withArgs(args);
      }

      @Override
      public <E> E withoutArgs() {
        return withArgs();
      }

      @Override
      public Invoker handlingExceptionsWith(ExceptionHandler handler) {
        this.handler = handler;
        return this;
      }

    };
  }

  /**
   * Invokes a collection of methods that have the same parameters. The access way (static
   * or non-static) does not matter.
   * <p/>
   * The return type will be a {@link Collection} of {@link Object objects} containing the
   * values based on the iteration order.
   *
   * @param methods the methods to invoke.
   *
   * @return a component for invoking the methods.
   */
  public static MethodInvoker invoke(final Collection<Method> methods) {
    return new MethodInvoker() {

      private Object target;

      private ExceptionHandler handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;

      public Invoker in(Object instance) {
        this.target = instance;
        return this;
      }

      public <E> E withArgs(Object... args) {
        Collection results = new ArrayList();
        for (Method method : methods) {
          if (isStatic(method)) {
            results.add(invoke(method).handlingExceptionsWith(handler).withArgs(args));
          } else {
            results.add(invoke(method).in(target).handlingExceptionsWith(handler).withArgs(args));
          }
        }
        return (E) results;
      }

      public <E> E withoutArgs() {
        return (E) withArgs();
      }


      @Override
      public Invoker handlingExceptionsWith(ExceptionHandler handler) {
        this.handler = handler;
        return this;
      }

    };
  }

  /**
   * Creates a new instance of the given type by locating the proper constructor based on
   * the given arguments.
   *
   * @param type                 the instance type
   * @param constructorArguments the arguments to call the constructor.
   *
   * @return a new instance of the give type
   *
   * @since 2.5
   */
  public static <E> E newInstanceOf(final Class<E> type, final Object... constructorArguments) {
    try {
      if (constructorArguments.length == 0) {
        Constructor<?> constructor = reflect().constructor().withoutParameters().in(type);
        return (E) invoke(constructor).withoutArgs();
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
        return (E) invoke(foundConstructor).withArgs(constructorArguments);
      }
      Set<Constructor<?>> constructors = reflect().constructors().in(type);
      Predicate<Constructor<?>> matchingConstructor = new Predicate<Constructor<?>>() {

        public boolean evaluate(Constructor<?> constructor) {
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
        }
      };
      foundConstructor = Find.the(matchingConstructor).in(constructors);
      if (foundConstructor != null) {
        return (E) invoke(foundConstructor).withArgs(constructorArguments);
      }
      throw new ReflectionException("No constructor found");
    } catch (NonUniqueMatchException e) {
      throw new ReflectionException(e);
    }
  }

  /**
   * @return an iterable Class hierarchy for use in "foreach" loops.
   *
   * @see ClassIterator
   * @since 4.0
   */
  public static Iterable<Class> hierarchyOf(final Object target) {
    return new Iterable<Class>() {
      @Override
      public Iterator<Class> iterator() {
        return new ClassIterator(target);
      }
    };
  }

}

