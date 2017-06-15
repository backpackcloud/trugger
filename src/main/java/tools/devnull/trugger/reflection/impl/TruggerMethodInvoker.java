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
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.reflection.MethodInvoker;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.reflection.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A default implementation for invoking {@link Method} objects.
 *
 * @author Marcelo Guimarães
 */
public class TruggerMethodInvoker implements MethodInvoker {

  private final Method method;
  private final Object instance;

  public TruggerMethodInvoker(final Method method) {
    if (!method.isAccessible()) {
      Reflection.setAccessible(method);
    }
    this.method = method;
    this.instance = null;
  }

  public TruggerMethodInvoker(Method method, Object instance) {
    this.method = method;
    this.instance = instance;
  }

  public MethodInvoker in(Object instance) {
    return new TruggerMethodInvoker(method, instance);
  }

  public <E> E withArgs(Object... args) {
    try {
      return (E) method.invoke(instance, args);
    } catch (InvocationTargetException e) {
      throw new ReflectionException(e.getCause());
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  public <E> E withoutArgs() {
    return withArgs();
  }

}
