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
package net.sf.trugger.reflection;

import java.lang.reflect.Method;

import net.sf.trugger.Invoker;
import net.sf.trugger.Result;

/**
 * Interface that defines a class capable of invoking a {@link Method}.
 * <p>
 * For invoking a static method, you don't need to specify any instance:
 * 
 * <pre>
 * MyType result = {@link Reflection#invoke(Method)}.withoutArgs();
 * </pre>
 * 
 * For a non-static method, you must specify an instance:
 * 
 * <pre>
 * MyType result = {@link Reflection#invoke(Method)}.on(instance).withArgs(arg1, arg2);
 * </pre>
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface MethodInvoker extends Invoker, Result<Invoker, Object> {
  
  /**
   * Indicates the instance that the method must be invoked. If this method is
   * not called before the invocation, then the method will be treated as a
   * <i>static</i> method.
   * 
   * @param instance
   *          the instance for invocation.
   * @return the component for invoking the method on the given instance.
   */
  Invoker in(Object instance);
  
}
