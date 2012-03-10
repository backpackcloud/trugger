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

import static net.sf.trugger.iteration.Iteration.selectFrom;

import java.lang.reflect.Method;
import java.util.Set;

import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.reflection.ReflectionException;
import net.sf.trugger.reflection.ReflectionPredicates;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerNoNamedMethodSelector extends TruggerMethodSelector {

  public TruggerNoNamedMethodSelector() {
    super(null);
  }

  @Override
  public Method in(Object target) {
    MethodsFinder finder = new MethodsFinder();
    MembersSelector<Method> selector = new MembersSelector<Method>(finder);
    if (useHierarchy()) {
      selector.useHierarchy();
    }
    Set<Method> set = selector.in(target);
    try {
      Class<?>[] parameterTypes = parameterTypes();
      if (parameterTypes != null) {
        builder().add(ReflectionPredicates.withParameters(parameterTypes));
      }
      return selectFrom(set).elementMatching(builder().predicate());
    } catch (SearchException e) {
      throw new ReflectionException(e);
    }
  }

}
