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

import org.atatec.trugger.iteration.NonUniqueMatchException;
import org.atatec.trugger.reflection.ReflectionException;

import java.lang.reflect.Field;
import java.util.Set;

/** @author Marcelo Varella Barca Guimarães */
public class TruggerNoNamedFieldSelector extends TruggerFieldSelector {

  public TruggerNoNamedFieldSelector(MemberFindersRegistry registry) {
    super(registry);
  }

  @Override
  public Field in(Object target) {
    MembersSelector<Field> selector = new MembersSelector<Field>(registry.fieldsFinder());
    if (useHierarchy()) {
      selector.useHierarchy();
    }
    Set<Field> fields = selector.in(target);
    try {
      return builder().findIn(fields);
    } catch (NonUniqueMatchException e) {
      throw new ReflectionException(e);
    }
  }

}
