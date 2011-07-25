/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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

import static net.sf.trugger.reflection.Reflection.constructor;
import static net.sf.trugger.reflection.Reflection.invoke;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;

/**
 * This class is a proxy that can track method invocations.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.5
 */
public class InvocationTrackerInterceptor extends Interceptor {

  private List<InterceptionContext> contexts = new ArrayList<InterceptionContext>();

  @Override
  protected Object intercept() throws Throwable {
    contexts.add(context());
    Class<?> returnType = method().getReturnType();
    if (Void.TYPE.equals(returnType)) {
      return null;
    }
    if ("getAnnotation".equals(method().getName())
        && AnnotatedElement.class.isAssignableFrom(method().getDeclaringClass())) {
      return createProxy().over((Class<?>) args()[0]);
    }
    if (returnType.isInterface()) {
      return createProxy().over(returnType);
    }
    if (ReflectionPredicates.FINAL_CLASS.evaluate(returnType)) {
      return null;
    }
    Constructor<?> constructor = constructor().withAccess(Access.PUBLIC).withoutParameters().in(returnType);
    if (constructor != null) {
      return createProxy().over(returnType);
    }
    return null;
  }

  /**
   * Clear the tracked invocations and starts to track the next invocations.
   */
  public void track() {
    contexts = new ArrayList<InterceptionContext>();
  }

  /**
   * @return the tracked invocations.
   */
  public List<InterceptionContext> trackedContexts() {
    return new ArrayList<InterceptionContext>(contexts);
  }

  /**
   * Resolves the value by invoking the tracked methods on the given target.
   *
   * @since 2.6
   */
  public <E> E resolveFor(Object target) {
    return (E) resolve(target, contexts);
  }

  /**
   * Resolves the value by invoking the tracked methods on the given target.
   *
   * @since 2.6
   */
  public static <E> E resolve(Object target, List<InterceptionContext> contexts) {
    Object objectValue = target;
    for (InterceptionContext context : contexts) {
      objectValue = invoke(context.method).in(objectValue).withArgs(context.args);
    }
    return (E) objectValue;
  }

}
