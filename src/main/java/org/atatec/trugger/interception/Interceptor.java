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
package org.atatec.trugger.interception;

import org.atatec.trugger.exception.ExceptionHandler;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.util.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * A base class to create Proxies to interfaces.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
public final class Interceptor implements InvocationHandler {

  private Object target;
  private Class[] interfaces;
  private ClassLoader classloader;
  private Interception action;
  private ExceptionHandler handler;

  private Interceptor(Object target) {
    Class<?> targetClass = Utils.resolveType(target);
    this.classloader = targetClass.getClassLoader();
    this.target = target;
    loadInterfaces();
  }

  private Interceptor(Class[] interfaces) {
    this.interfaces = interfaces;
    this.classloader = ClassLoader.getSystemClassLoader();
  }

  public Interceptor with(ClassLoader classloader) {
    this.classloader = classloader;
    return this;
  }

  public Interceptor of(Object target) {
    this.target = target;
    loadInterfaces();
    return this;
  }

  private void loadInterfaces() {
    Set<Class<?>> classes = Reflection.reflect().interfaces().in(target);
    interfaces = classes.toArray(new Class[classes.size()]);
  }

  public Interceptor onCall(Interception action) {
    this.action = action;
    return this;
  }

  public Interceptor onError(ExceptionHandler handler) {
    this.handler = handler;
    return this;
  }

  public <E> E proxy() {
    return (E) Proxy.newProxyInstance(classloader, interfaces, Interceptor.this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    InterceptionContext context = new InterceptionContext(target, proxy,
        method, args);
    try {
      return action.intercept(context);
    } catch (Throwable e) {
      if (handler != null) {
        handler.handle(e);
        return null;
      } else {
        throw e;
      }
    }
  }

  public static Interceptor intercept(Class interfaceClass) {
    return new Interceptor(new Class[]{interfaceClass});
  }

  public static Interceptor intercept(Class... interfaces) {
    return new Interceptor(interfaces);
  }

  public static Interceptor intercept(Object target) {
    return new Interceptor(target);
  }
}
