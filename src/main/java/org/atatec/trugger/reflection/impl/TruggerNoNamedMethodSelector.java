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

import org.atatec.trugger.iteration.SearchException;
import org.atatec.trugger.reflection.MethodPredicates;
import org.atatec.trugger.reflection.ReflectionException;

import java.lang.reflect.Method;
import java.util.Set;

import static org.atatec.trugger.iteration.Iteration.selectFrom;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerNoNamedMethodSelector extends TruggerMethodSelector {

  public TruggerNoNamedMethodSelector(MemberFindersRegistry registry) {
    super(registry);
  }

  @Override
  public Method in(Object target) {
    MembersSelector<Method> selector = new MembersSelector<Method>(registry.methodsFinder());
    if (useHierarchy()) {
      selector.useHierarchy();
    }
    Set<Method> set = selector.in(target);
    try {
      Class<?>[] parameterTypes = parameterTypes();
      if (parameterTypes != null) {
        builder().add(MethodPredicates.takes(parameterTypes));
      }
      return selectFrom(set).element(builder().predicate());
    } catch (SearchException e) {
      throw new ReflectionException(e);
    }
  }

}
