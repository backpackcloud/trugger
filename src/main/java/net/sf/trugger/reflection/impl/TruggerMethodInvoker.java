/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.reflection.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.trugger.reflection.MethodInvoker;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.reflection.ReflectionException;

/**
 * A default implementation for invoking {@link Method} objects.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerMethodInvoker implements MethodInvoker {
  
  private final Method method;
  
  private Object instance;
  
  public TruggerMethodInvoker(final Method method) {
    if (!method.isAccessible()) {
      Reflection.setAccessible(method);
    }
    this.method = method;
  }
  
  public MethodInvoker in(Object instance) {
    this.instance = instance;
    return this;
  }
  
  public <E> E withArgs(Object... args) {
    try {
      return (E) method.invoke(instance, args);
    } catch (InvocationTargetException e) {
      throw new ReflectionException(e.getCause());
    } catch (IllegalAccessException e) {
      throw new ReflectionException(e);
    } catch (IllegalArgumentException e) {
      throw new ReflectionException(e);
    }
  }
  
  public <E> E withoutArgs() {
    return (E) withArgs();
  }
  
}
