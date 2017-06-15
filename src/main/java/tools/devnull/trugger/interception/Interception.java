/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.interception;

import tools.devnull.trugger.util.ImplementationLoader;

/**
 * A class for helping create proxy instances
 *
 * @since 5.0
 */
public final class Interception {

  private static final InterceptorFactory factory;

  static {
    factory = ImplementationLoader.get(InterceptorFactory.class);
  }

  private Interception() {
  }

  /**
   * Creates an interceptor for the given interface
   *
   * @param interfaceClass the interface to intercept
   * @return a component to configure the interception
   */
  public static Interceptor intercept(Class interfaceClass) {
    return factory.createInterceptor(interfaceClass);
  }

  /**
   * Creates an interceptor for the given interfaces
   *
   * @param interfaces the interfaces to intercept
   * @return a component to configure the interception
   */
  public static Interceptor intercept(Class... interfaces) {
    return factory.createInterceptor(interfaces);
  }

  /**
   * Creates an interceptor for the given target.
   *
   * @param target the target to intercept
   * @return a component to configure the interception
   */
  public static Interceptor intercept(Object target) {
    return factory.createInterceptor(target);
  }

}
