/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.SelectionResult;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.reflection.ReflectionPredicates;
import tools.devnull.trugger.selector.MethodSelector;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A default implementation for the method selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerMethodSelector implements MethodSelector {

  private final String name;
  private final MemberFindersRegistry registry;
  private final Class[] parameterTypes;
  private final Predicate<? super Method> predicate;
  private final Function<Class, Iterable<Class>> function;

  public TruggerMethodSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
    this.parameterTypes = null;
    this.predicate = null;
    this.function = Collections::singletonList;
  }

  public TruggerMethodSelector(String name,
                               MemberFindersRegistry registry,
                               Class[] parameterTypes,
                               Predicate<? super Method> predicate,
                               Function<Class, Iterable<Class>> function) {
    this.name = name;
    this.registry = registry;
    this.parameterTypes = parameterTypes;
    this.predicate = predicate;
    this.function = function;
  }

  public MethodSelector withParameters(Class<?>... parameterTypes) {
    return new TruggerMethodSelector(this.name, this.registry, parameterTypes, this.predicate, this.function);
  }

  public MethodSelector withoutParameters() {
    return withParameters();
  }

  public MethodSelector deep() {
    return new TruggerMethodSelector(this.name, this.registry, parameterTypes, predicate, Reflection::hierarchyOf);
  }

  @Override
  public MethodSelector filter(Predicate<? super Method> predicate) {
    return new TruggerMethodSelector(name, registry, parameterTypes, predicate, function);
  }

  public SelectionResult<Method> from(Object target) {
    if (parameterTypes != null) {
      return new MemberSelector<>(registry.methodFinder(name, parameterTypes), predicate, function).selectFrom(target);
    }
    MembersSelector<Method> selector = new MembersSelector<>(registry.methodsFinder(), predicate, function);
    return new SelectionResult<>(target, selector.selectFrom(target).stream()
        .filter(ReflectionPredicates.named(name))
        .findAny().orElse(null));
  }

}
