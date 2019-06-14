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

package io.backpackcloud.trugger.interception.impl;

import io.backpackcloud.trugger.interception.InterceptionContext;
import io.backpackcloud.trugger.interception.InterceptionFailHandler;
import io.backpackcloud.trugger.interception.InterceptionHandler;
import io.backpackcloud.trugger.interception.Interceptor;
import io.backpackcloud.trugger.reflection.Reflection;
import io.backpackcloud.trugger.util.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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
    List<Class> classes = Reflection.reflect().interfacesOf(target);
    this.interfaces = classes.toArray(new Class[classes.size()]);
    this.action = InterceptionHandler.delegate();
    this.handler = InterceptionFailHandler.throwError();
  }

  public TruggerInterceptor(Class[] interfaces) {
    this.interfaces = interfaces;
    this.classloader = ClassLoader.getSystemClassLoader();
    this.target = null;
    this.action = InterceptionHandler.delegate();
    this.handler = InterceptionFailHandler.throwError();
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
  public TruggerInterceptor on(Object target) {
    return new TruggerInterceptor(
        target, interfaces, classloader, action, handler
    );
  }

  @Override
  public TruggerInterceptor onCall(InterceptionHandler action) {
    return new TruggerInterceptor(
        target, interfaces, classloader, action, handler
    );
  }

  @Override
  public TruggerInterceptor onFail(InterceptionFailHandler handler) {
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
      return handler.handle(context, e);
    }
  }

}
