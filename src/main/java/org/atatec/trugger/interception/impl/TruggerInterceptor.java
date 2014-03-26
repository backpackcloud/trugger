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

package org.atatec.trugger.interception.impl;

import org.atatec.trugger.interception.InterceptionContext;
import org.atatec.trugger.interception.InterceptionFailHandler;
import org.atatec.trugger.interception.InterceptionHandler;
import org.atatec.trugger.interception.Interceptor;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.util.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

public class TruggerInterceptor implements InvocationHandler, Interceptor {

  private final Object target;
  private final Class[] interfaces;
  private final ClassLoader classloader;
  private final InterceptionHandler action;
  private final InterceptionFailHandler handler;

  public TruggerInterceptor(Object target) {
    Class<?> targetClass = Utils.resolveType(target);
    this.classloader = targetClass.getClassLoader();
    this.target = target;
    Set<Class<?>> classes = Reflection.reflect().interfaces().in(target);
    this.interfaces = classes.toArray(new Class[classes.size()]);
    this.action = (context) -> context.invokeMethod();
    this.handler = (context, error) -> {
      throw error;
    };
  }

  public TruggerInterceptor(Class[] interfaces) {
    this.interfaces = interfaces;
    this.classloader = ClassLoader.getSystemClassLoader();
    this.target = null;
    this.action = (context) -> context.invokeMethod();
    this.handler = (context, error) -> {
      throw error;
    };
  }

  public TruggerInterceptor(Object target, Class[] interfaces,
                            ClassLoader classloader, InterceptionHandler action,
                            InterceptionFailHandler handler) {
    this.target = target;
    this.interfaces = interfaces;
    this.classloader = classloader;
    this.action = action;
    this.handler = handler;
  }

  @Override
  public TruggerInterceptor with(ClassLoader classloader) {
    return new TruggerInterceptor(
        target, interfaces, classloader, action, handler
    );
  }

  @Override
  public TruggerInterceptor of(Object target) {
    Set<Class<?>> classes = Reflection.reflect().interfaces().in(target);
    return new TruggerInterceptor(
        target, classes.toArray(new Class[classes.size()]), classloader,
        action, handler
    );
  }

  @Override
  public TruggerInterceptor onCall(InterceptionHandler action) {
    return new TruggerInterceptor(
        target, interfaces, classloader, action, handler
    );
  }

  @Override
  public TruggerInterceptor onError(InterceptionFailHandler handler) {
    return new TruggerInterceptor(
        target, interfaces, classloader, action, handler
    );
  }

  @Override
  public <E> E proxy() {
    return (E) Proxy.newProxyInstance(classloader, interfaces, this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    InterceptionContext context = new InterceptionContextImpl(target, proxy,
        method, args);
    try {
      return action.intercept(context);
    } catch (Throwable e) {
      if (handler != null) {
        return handler.handle(context, e);
      } else {
        throw e;
      }
    }
  }

}
