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
package net.sf.trugger.interception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.util.Utils;

/**
 * A base class to create Proxies to other classes using the CGLib.
 * <p>
 * This is how this class works:
 * <ol>
 * <li>When a method is intercepted, the Interceptor encapsulates the method and
 * the arguments in a {@link InterceptionContext} that becomes available to the
 * class by the {@link #context()}.
 * <li>The {@link #intercept() intercept} method is called.
 * <li>The {@link InterceptionContext} is released.
 * </ol>
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class Interceptor implements MethodInterceptor, ProxyFactory {

  /**
   * The target object
   */
  protected Object target;
  /**
   * The interfaces configured for interception.
   */
  private final Set<Class<?>> interfaces = new HashSet<Class<?>>();
  /**
   * Holds the contexts.
   */
  private ThreadLocal<InterceptionContext> threadLocal = new ThreadLocal<InterceptionContext>();
  /**
   * A flag that indicates if the proxy should subclass the target.
   */
  private boolean subclass;

  /**
   * Creates a new Interceptor
   */
  public Interceptor() {
    super();
  }

  /**
   * @return the interception context.
   */
  protected final InterceptionContext context() {
    return threadLocal.get();
  }

  /**
   * @return InterceptionContext#proxy
   */
  protected final Object proxy() {
    return context().proxy;
  }

  /**
   * @return InterceptionContext#methodProxy
   */
  private final MethodProxy methodProxy() {
    return context().methodProxy;
  }

  /**
   * @return InterceptionContext#method
   */
  protected final Method method() {
    return context().method;
  }

  /**
   * @return InterceptionContext#args
   */
  protected final Object[] args() {
    return context().args;
  }

  /**
   * @return the argument at the given index.
   * @since 2.3
   */
  protected final Object arg(int index) {
    return args()[index];
  }

  public ProxyCreator createProxy() {
    return new Creator();
  }

  public final Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    Reflection.setAccessible(method);
    threadLocal.set(new InterceptionContext(proxy, method, args, methodProxy));
    try {
      return intercept();
    } finally {
      threadLocal.remove();
    }
  }

  /**
   * Invokes the intercepted method on the target object.
   *
   * @param target
   *          the target object
   * @return the return of the method
   * @throws Throwable
   *           if an error occurs in the method.
   */
  protected final Object invokeMethod(Object target) throws Throwable {
    if (subclass) {
      return methodProxy().invokeSuper(target, args());
    }
    try {
      Method targetMethod = getTargetMethod(target);
      Reflection.setAccessible(targetMethod);
      return targetMethod.invoke(target, args());
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  /**
   * Invokes the intercepted method on the {@link #target} object.
   *
   * @see #invokeMethod(Object)
   */
  protected final Object invokeMethod() throws Throwable {
    if (subclass) {
      return invokeMethod(proxy());
    }
    return invokeMethod(this.target);
  }

  /**
   * This method is called when a target's method is intercepted.
   */
  protected Object intercept() throws Throwable {
    return invokeMethod();
  }

  /**
   * @return the intercepted method declared in the {@link #target}.
   */
  protected final Method getTargetMethod() {
    return getTargetMethod(target);
  }

  /**
   * @param target
   *          the target to get the method.
   * @return the intercepted method declared in the given target.
   */
  protected final Method getTargetMethod(Object target) {
    Method method = method();
    String name = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    Method targetMethod = Reflection.reflect().method(name).recursively().withParameters(parameterTypes).in(target);
    if (targetMethod.isBridge()) {
      return Reflection.reflect().bridgedMethodFor(targetMethod);
    }
    return targetMethod;
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
    
    public <E> E over(Class<?> clazz) {
      if(clazz.isInterface()) {
        return createProxy().implementing(clazz).withoutTarget();
      }
      return createProxy().extending(clazz);
    }

    public <E> E withTarget(Object target) {
      Interceptor.this.target = target;
      Interceptor.this.subclass = false;
      return (E) create();
    }

    public <E> E withoutTarget() {
      Interceptor.this.target = new Object();
      Interceptor.this.subclass = false;
      return (E) create();
    }

    public <E> E extending(Class<?> subclass) {
      target = subclass;
      Interceptor.this.subclass = true;
      return (E) create();
    }

    /**
     * Creates a Proxy for all the specified interfaces using the specified
     * ClassLoader. If subclass is <code>true</code>, the proxy will be a
     * subclass of the target.
     */
    private <E> E create() {
      if (classLoader == null) {
        Class<?> targetClass = Utils.resolveType(target);
        classLoader = targetClass.getClassLoader();
        if (classLoader == null) {
          classLoader = ClassLoader.getSystemClassLoader();
        }
      }
      if (computeTargetInterfaces) {
        interfaces.addAll(Reflection.reflect().interfaces().in(target));
      }
      Enhancer enhancer = new Enhancer();
      Class<?> targetClass = Utils.resolveType(target);
      if (subclass) {
        enhancer.setSuperclass(targetClass);
      }
      enhancer.setClassLoader(classLoader);
      if (!interfaces.isEmpty()) {
        enhancer.setInterfaces(interfaces.toArray(new Class[interfaces.size()]));
      }
      enhancer.setCallback(Interceptor.this);
      return (E) enhancer.create();
    }
  }

}
