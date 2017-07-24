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
package tools.devnull.trugger.reflection;

import tools.devnull.trugger.Invoker;
import java.lang.reflect.Method;

/**
 * Interface that defines a class capable of invoking a {@link Method}.
 * <p>
 * For invoking a static method, you don't need to specify that instance:
 * <p>
 * <pre>
 * MyType result = {@link Reflection#invoke(Method)}.withoutArgs();
 * </pre>
 * <p>
 * For a non-static method, you must specify an instance:
 * <p>
 * <pre>
 * MyType result = {@link Reflection#invoke(Method)}.on(instance).withArgs(arg1, arg2);
 * </pre>
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public interface MethodInvoker extends Invoker {

  /**
   * Indicates the instance that the method must be invoked. If this method is not called
   * before the invocation, then the method will be treated as a <i>static</i> method.
   *
   * @param instance the instance for invocation.
   * @return the component for invoking the method on the given instance.
   */
  Invoker on(Object instance);

}
