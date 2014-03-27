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

import org.atatec.trugger.selector.MethodsSelector;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation for the methods selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerMethodsSelector implements MethodsSelector {

  private final MembersFinder<Method> finder;
  private final Predicate<? super Method> predicate;
  private final boolean recursively;

  public TruggerMethodsSelector(MembersFinder<Method> finder) {
    this.finder = finder;
    this.predicate = null;
    this.recursively = false;
  }

  public TruggerMethodsSelector(MembersFinder<Method> finder,
                                Predicate<? super Method> predicate,
                                boolean recursively) {
    this.finder = finder;
    this.predicate = predicate;
    this.recursively = recursively;
  }

  @Override
  public MethodsSelector filter(Predicate<? super Method> predicate) {
    return new TruggerMethodsSelector(finder, predicate, recursively);
  }

  @Override
  public MethodsSelector recursively() {
    return new TruggerMethodsSelector(finder, predicate, true);
  }

  @Override
  public List<Method> in(Object target) {
    return new MembersSelector<>(finder, predicate, recursively).in(target);
  }
}
