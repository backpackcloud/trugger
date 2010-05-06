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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.reflection.ReflectionPredicates;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class GetterFinder implements MemberFinder<Method> {

  private final String name;

  public GetterFinder(String name) {
    this.name = name;
  }

  public Method find(Class<?> type) throws Exception {
    Set<Method> methods = new HashSet<Method>(Arrays.asList(type.getDeclaredMethods()));
    Iteration.retainFrom(methods).elementsMatching(ReflectionPredicates.getterFor(name));
    if (!methods.isEmpty()) {
      return methods.iterator().next();
    }
    throw new NoSuchMethodException();
  }
}
