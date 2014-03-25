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
package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.Invoker;
import org.atatec.trugger.exception.ExceptionHandler;
import org.atatec.trugger.exception.ExceptionHandlers;
import org.atatec.trugger.reflection.ConstructorInvoker;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.reflection.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A default implementation for invoking {@link Constructor} objects.
 *
 * @author Marcelo Guimarães
 */
public class TruggerConstructorInvoker implements ConstructorInvoker {

  private final Constructor<?> constructor;
  private final ExceptionHandler handler;

  public TruggerConstructorInvoker(final Constructor<?> constructor) {
    if (!constructor.isAccessible()) {
      Reflection.setAccessible(constructor);
    }
    this.constructor = constructor;
    this.handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;
  }

  public TruggerConstructorInvoker(Constructor<?> constructor,
                                   ExceptionHandler handler) {
    this.constructor = constructor;
    this.handler = handler;
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
    return withArgs();
  }

  @Override
  public Invoker onError(ExceptionHandler handler) {
    return new TruggerConstructorInvoker(constructor, handler);
  }

}
