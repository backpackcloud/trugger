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
import org.atatec.trugger.reflection.MethodInvoker;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.selector.MethodSelector;

import java.lang.reflect.Method;

public class MethodSelectorInvoker implements MethodInvoker {

  private final MethodSelector selector;
  private final Object target;
  private final ExceptionHandler handler;

  public MethodSelectorInvoker(MethodSelector selector) {
    this.selector = selector;
    this.target = null;
    this.handler = ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER;
  }

  public MethodSelectorInvoker(MethodSelector selector, Object target,
                               ExceptionHandler handler) {
    this.selector = selector;
    this.target = target;
    this.handler = handler;
  }

  @Override
  public Invoker in(Object instance) {
    return new MethodSelectorInvoker(selector, instance, handler);
  }

  @Override
  public <E> E withArgs(Object... args) {
    Method method = selector.in(target);
    return Reflection.invoke(method).in(target).onError(handler).withArgs(args);
  }

  @Override
  public <E> E withoutArgs() {
    return withArgs();
  }

  @Override
  public Invoker onError(ExceptionHandler handler) {
    return new MethodSelectorInvoker(selector, target, handler);
  }

}
