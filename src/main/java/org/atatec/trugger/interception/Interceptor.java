/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.util.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.reflect;

/**
 * A base class to create Proxies to interfaces.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class Interceptor implements InvocationHandler, ProxyFactory {

  /** The target object */
  private Object target;
  /** The interfaces configured for interception. */
  private final Set<Class<?>> interfaces = new HashSet<Class<?>>();

  /** Creates a new Interceptor */
  public Interceptor() {
    super();
  }

  public ProxyCreator createProxy() {
    return new Creator();
  }

  protected final Object target() {
    return target;
  }

  @Override
  public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return intercept(new InterceptionContext(proxy, method, args));
  }

  /** This method is called when a target's method is intercepted. */
  protected Object intercept(InterceptionContext context) throws Throwable {
    return context.invokeMethod(target);
  }

  private class Creator implements ProxyCreator {

    private boolean computeTargetInterfaces;
    private ClassLoader classLoader;

    private Creator() {

    }

    public ProxyCreator withClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
      return this;
    }

    public ProxyCreator implementing(Class<?>... interfaces) {
      Interceptor.this.interfaces.addAll(Arrays.asList(interfaces));
      return this;
    }

    public ProxyCreator forAllInterfaces() {
      this.computeTargetInterfaces = true;
      return this;
    }

    public <E> E withTarget(Object target) {
      Interceptor.this.target = target;
      return (E) create();
    }

    public <E> E withoutTarget() {
      Interceptor.this.target = null;
      return (E) create();
    }

    private <E> E create() {
      if (classLoader == null) {
        if (target != null) {
          Class<?> targetClass = Utils.resolveType(target);
          classLoader = targetClass.getClassLoader();
        }
        if (classLoader == null) {
          classLoader = ClassLoader.getSystemClassLoader();
        }
      }
      if (computeTargetInterfaces) {
        interfaces.addAll(reflect().interfaces().in(target));
      }
      return (E) Proxy.newProxyInstance(
        classLoader,
        interfaces.toArray(new Class[interfaces.size()]),
        Interceptor.this);
    }
  }

}
