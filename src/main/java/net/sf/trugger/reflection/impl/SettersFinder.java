/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.reflection.ReflectionPredicates;

/**
 * A finder for setter {@link Method} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class SettersFinder implements MembersFinder<Method> {

  private final String name;

  public SettersFinder(String name) {
    this.name = name;
  }

  public Method[] find(Class<?> type) {
    List<Method> methods = new ArrayList<Method>(Arrays.asList(type.getDeclaredMethods()));
    Iteration.retainFrom(methods).elementsMatching(ReflectionPredicates.setterFor(name));
    return methods.toArray(new Method[methods.size()]);
  }
}
