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
package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.iteration.Iteration;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.atatec.trugger.reflection.ReflectionPredicates.isSetterOf;

/**
 * A finder for setter {@link Method} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class SettersFinder implements MembersFinder<Method> {

  private final String name;
  private MembersFinder finder;

  public SettersFinder(String name, MembersFinder<Method> finder) {
    this.name = name;
    this.finder = finder;
  }

  public Collection<Method> find(Class<?> type) {
    return Iteration.selectFrom(finder.find(type)).anyThat(isSetterOf(name));
  }
}
