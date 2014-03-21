/*
 * Copyright 2009-2012 Marcelo Guimarães
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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.atatec.trugger.reflection.ReflectionPredicates.getterOf;

/**
 * @author Marcelo Guimarães
 */
public class GetterFinder implements MemberFinder<Method> {

  private final String name;
  private MembersFinder<Method> finder;

  public GetterFinder(String name, MembersFinder<Method> finder) {
    this.name = name;
    this.finder = finder;
  }

  public Method find(Class<?> type) throws Exception {
    Set<Method> methods = new HashSet<Method>(finder.find(type));
    return methods.stream().filter(getterOf(name)).findAny().orElse(null);
  }
}
