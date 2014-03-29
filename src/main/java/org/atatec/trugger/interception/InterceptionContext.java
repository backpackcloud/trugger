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

import java.lang.reflect.Method;

/**
 * Interface that holds a context of a method interception.
 *
 * @since 5.0
 */
public interface InterceptionContext {

  /**
   * @return the interceptor target or <code>null</code> if is not defined.
   */
  Object target();

  /**
   * @return the arguments passed in the method invocation on the proxy instance
   * or <code>null</code> if the method takes no parameters.
   */
  Object[] args();

  /**
   * @return the proxy instance that the method was invoked on
   */
  Object proxy();

  /**
   * @return the <code>Method</code> instance corresponding to the method
   * invoked on the proxy instance
   */
  Method method();

  /**
   * @return the intercepted method declared on target
   */
  default Method targetMethod() {
    return methodOn(target());
  }

  /**
   * Invokes the intercepted method on the {@link #target() target} object.
   *
   * @return the return of the method
   * @throws Throwable if an error occurs in the method.
   */
  Object invoke() throws Throwable;

  /**
   * Invokes the intercepted method on the given target object.
   *
   * @param target the target object
   * @return the return of the method
   * @throws Throwable if an error occurs in the method.
   */
  Object invokeOn(Object target) throws Throwable;

  /**
   * @param target the target to get the method.
   * @return the intercepted method declared in the given target.
   */
  Method methodOn(Object target);

}
