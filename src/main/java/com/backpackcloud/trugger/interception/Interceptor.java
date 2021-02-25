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

package com.backpackcloud.trugger.interception;

/**
 * Interface that defines a component capable of creating proxy objects
 * to intercept methods.
 *
 * @since 5.0
 */
public interface Interceptor {

  /**
   * Defines the ClassLoader that should be used to create the proxy instance.
   *
   * @param classloader the ClassLoader to create the proxy
   * @return a new Interceptor that uses the given ClassLoader
   */
  Interceptor with(ClassLoader classloader);

  /**
   * Defines the target object to intercept. The target will be forwarded to the
   * handler by the {@link InterceptionContext}.
   *
   * @param target the target to intercept
   * @return a new Interceptor that uses the given target
   */
  Interceptor on(Object target);

  /**
   * Defines the action to execute on method interception.
   *
   * @param action the action to execute
   * @return a new Interceptor that uses the given action
   */
  Interceptor onCall(InterceptionHandler action);

  /**
   * Defines a handler to deal with errors in interception.
   *
   * @param handler the handler to use
   * @return a new Interceptor that uses the given handler
   */
  Interceptor onFail(InterceptionFailHandler handler);

  /**
   * Creates the proxy instance based on the components configured.
   *
   * @return the proxy instance
   */
  <E> E proxy();

}
