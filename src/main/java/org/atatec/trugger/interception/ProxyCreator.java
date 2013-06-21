/*
 * Copyright 2009-2012 Marcelo Guimarães
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

/**
 * Interface that defines a class capable of configure a Proxy.
 * 
 * @author Marcelo Guimarães
 * @since 2.1
 */
public interface ProxyCreator {
  
  /**
   * @param classLoader
   *          the ClassLoader to create the proxy.
   * @return a reference to this object.
   * @since 2.3
   */
  ProxyCreator withClassLoader(ClassLoader classLoader);
  
  /**
   * Configures the proxy to encapsulate the given target.
   * 
   * @param target
   *          the target to encapsulate.
   * @return the created proxy.
   */
  ProxyCreator over(Object target);
  
  /**
   * Configures the proxy to implement all the target interfaces, the explicitly
   * (declared interfaces) and implicitly (subclasses' interfaces).
   * 
   * @return a reference to this object.
   */
  <E> E forAllInterfaces();
  
  /**
   * Configures the proxy to implement these interfaces.
   * 
   * @param interfaces
   *          the interfaces that the proxy must implement.
   * @return a reference to this object.
   */
  <E> E implementing(Class<?>... interfaces);
  
}
