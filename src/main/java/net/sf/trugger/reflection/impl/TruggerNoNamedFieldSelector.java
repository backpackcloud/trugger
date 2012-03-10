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

import java.lang.reflect.Field;
import java.util.Set;

import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.reflection.ReflectionException;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerNoNamedFieldSelector extends TruggerFieldSelector {

  public TruggerNoNamedFieldSelector() {
    super(null);
  }

  @Override
  public Field in(Object target) {
    FieldsFinder finder = new FieldsFinder();
    MembersSelector<Field> selector = new MembersSelector<Field>(finder);
    if(useHierarchy()) {
      selector.useHierarchy();
    }
    Set<Field> fields = selector.in(target);
    try {
      return selectFrom(fields).elementMatching(builder().predicate());
    } catch (SearchException e) {
      throw new ReflectionException(e);
    }
  }

}
