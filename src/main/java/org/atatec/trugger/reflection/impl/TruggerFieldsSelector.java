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

import org.atatec.trugger.selector.FieldsSelector;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A default implementation for the fields selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerFieldsSelector implements FieldsSelector {

  private final MembersFinder<Field> finder;
  private final Predicate<? super Field> predicate;
  private final boolean recursively;

  public TruggerFieldsSelector(MembersFinder<Field> finder) {
    this.finder = finder;
    this.predicate = null;
    this.recursively = false;
  }

  public TruggerFieldsSelector(MembersFinder<Field> finder,
                               Predicate<? super Field> predicate,
                               boolean recursively) {
    this.finder = finder;
    this.predicate = predicate;
    this.recursively = recursively;
  }

  @Override
  public FieldsSelector filter(Predicate<? super Field> predicate) {
    return new TruggerFieldsSelector(finder, predicate, recursively);
  }

  @Override
  public FieldsSelector recursively() {
    return new TruggerFieldsSelector(finder, predicate, true);
  }

  public Set<Field> in(Object target) {
    return new MembersSelector<>(finder, predicate, recursively).in(target);
  }

}
