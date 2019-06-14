/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger.reflection.impl;

import io.backpackcloud.trugger.reflection.MethodsSelector;
import io.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A default implementation for the methods selector.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class TruggerMethodsSelector implements MethodsSelector {

  private final MembersFinder<Method> finder;
  private final Predicate<? super Method> predicate;
  private final Function<Class, Iterable<Class>> function;

  public TruggerMethodsSelector(MembersFinder<Method> finder) {
    this.finder = finder;
    this.predicate = null;
    this.function = Collections::singletonList;
  }

  public TruggerMethodsSelector(MembersFinder<Method> finder,
                                Predicate<? super Method> predicate,
                                Function<Class, Iterable<Class>> function) {
    this.finder = finder;
    this.predicate = predicate;
    this.function = function;
  }

  @Override
  public MethodsSelector filter(Predicate<? super Method> predicate) {
    return new TruggerMethodsSelector(this.finder, predicate, this.function);
  }

  @Override
  public MethodsSelector deep() {
    return new TruggerMethodsSelector(finder, predicate, Reflection::hierarchyOf);
  }

  @Override
  public List<Method> from(Object target) {
    return new MembersSelector<>(finder, predicate, function).selectFrom(target);
  }

}
