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

import net.sf.trugger.Invoker;
import net.sf.trugger.exception.ExceptionHandler;
import net.sf.trugger.exception.ExceptionHandlers;
import net.sf.trugger.reflection.ConstructorInvoker;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.reflection.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A default implementation for invoking {@link Constructor} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerConstructorInvoker implements ConstructorInvoker {

  private final Constructor<?> constructor;

  private ExceptionHandler handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;

  public TruggerConstructorInvoker(final Constructor<?> constructor) {
    if (!constructor.isAccessible()) {
      Reflection.setAccessible(constructor);
    }
    this.constructor = constructor;
  }

  public <E> E withArgs(Object... args) {
    try {
      try {
        return (E) constructor.newInstance(args);
      } catch (InstantiationException e) {
        throw new ReflectionException(e);
      } catch (InvocationTargetException e) {
        throw new ReflectionException(e.getCause());
      } catch (IllegalAccessException e) {
        throw new ReflectionException(e);
      }
    } catch (RuntimeException e) {
      handler.handle(e);
      return null;
    }
  }

  public <E> E withoutArgs() {
    return (E) withArgs();
  }

  @Override
  public Invoker handlingWith(ExceptionHandler handler) {
    this.handler = handler;
    return this;
  }

}
