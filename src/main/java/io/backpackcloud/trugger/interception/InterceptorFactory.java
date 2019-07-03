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

package io.backpackcloud.trugger.interception;

/**
 * A factory to create interceptors.
 *
 * @since 5.0
 */
public interface InterceptorFactory {

  /**
   * Creates an interceptor for the given interface.
   *
   * @param interfaceClass the interface to intercept
   * @return the created interceptor
   */
  Interceptor createInterceptor(Class interfaceClass);

  /**
   * Creates an interceptor for the given interfaces.
   *
   * @param interfaces the interfaces to intercept
   * @return the created interceptor
   */
  Interceptor createInterceptor(Class[] interfaces);

  /**
   * Creates an interceptor for the given target. The target's
   * interfaces will be intercepted.
   *
   * @param target the target to intercept
   * @return the created interceptor
   */
  Interceptor createInterceptor(Object target);

}
