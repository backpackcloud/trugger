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

import tools.devnull.trugger.Invoker;
import tools.devnull.trugger.reflection.MethodInvoker;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.selector.MethodSelector;

import java.lang.reflect.Method;

public class MethodSelectorInvoker implements MethodInvoker {

  private final MethodSelector selector;
  private final Object target;

  public MethodSelectorInvoker(MethodSelector selector) {
    this.selector = selector;
    this.target = null;
  }

  public MethodSelectorInvoker(MethodSelector selector, Object target) {
    this.selector = selector;
    this.target = target;
  }

  @Override
  public Invoker in(Object instance) {
    return new MethodSelectorInvoker(selector, instance);
  }

  @Override
  public <E> E withArgs(Object... args) {
    Method method = selector.in(target);
    return Reflection.invoke(method).in(target).withArgs(args);
  }

  @Override
  public <E> E withoutArgs() {
    return withArgs();
  }

}
