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
package org.atatec.trugger.interception;

import org.atatec.trugger.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.atatec.trugger.reflection.Reflection.reflect;

/**
 * This class holds the parameters of a method interception.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
public class InterceptionContext {

  private static final Map<Class<?>, Object> nullValues;

  static {
    nullValues = new HashMap<Class<?>, Object>() {{
      put(byte.class, Byte.valueOf((byte) 0));
      put(short.class, Short.valueOf((short) 0));
      put(int.class, 0);
      put(long.class, 0L);
      put(char.class, Character.valueOf((char) 0));
      put(float.class, 0f);
      put(double.class, 0d);
      put(boolean.class, false);
    }};

  }

  private final Object target;

  /** The proxy instance that the method was invoked on */
  private final Object proxy;

  /**
   * The <code>Method</code> instance corresponding to the method invoked on the proxy
   * instance
   */
  private final Method method;

  /**
   * The values of the arguments passed in the method invocation on the proxy instance, or
   * <code>null</code> if interface method takes no arguments
   */
  private final Object[] args;

  /**
   * Creates a new InterceptionContext
   *
   * @param proxy  the proxy instance that the method was invoked on
   * @param method the <code>Method</code> instance corresponding to the method invoked on
   *               the proxy instance
   * @param args   the arguments passed in the method invocation on the proxy instance
   */
  public InterceptionContext(Object target, Object proxy, Method method, Object[] args) {
    this.target = target;
    this.proxy = proxy;
    this.method = method;
    this.args = args;
  }

  /**
   * Invokes the intercepted method on the {@link #target() target} object.
   *
   * @return the return of the method
   *
   * @throws Throwable if an error occurs in the method.
   */
  public Object invokeMethod() throws Throwable {
    return invokeMethod(target);
  }

  /**
   * Invokes the intercepted method on the given target object.
   *
   * @param target the target object
   *
   * @return the return of the method
   *
   * @throws Throwable if an error occurs in the method.
   */
  public Object invokeMethod(Object target) throws Throwable {
    try {
      Method targetMethod = methodOn(target);
      Reflection.setAccessible(targetMethod);
      return targetMethod.invoke(target, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * @param target the target to get the method.
   *
   * @return the intercepted method declared in the given target.
   */
  public Method methodOn(Object target) {
    String name = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    Method targetMethod = reflect().method(name).recursively().withParameters(parameterTypes).in(target);
    if (targetMethod.isBridge()) {
      return reflect().bridgedMethodFor(targetMethod);
    }
    return targetMethod;
  }

  /** @return the interceptor target or <code>null</code> if is not defined. */
  public Object target() {
    return target;
  }

  /** @return the arguments passed in the method invocation on the proxy instance */
  public Object[] args() {
    return args;
  }

  /** @return the proxy instance that the method was invoked on */
  public Object proxy() {
    return proxy;
  }

  /**
   * @return the <code>Method</code> instance corresponding to the method invoked on the
   *         proxy instance
   */
  public Method method() {
    return method;
  }

  /**
   * @return <null></null> if the method return type is an Object or a default primitive
   * value if it is primitive (false to boolean, 0 to long...).
   */
  public Object nullReturn() {
    return nullValues.get(method.getReturnType());
  }

}
