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
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.reflection.ReflectionPredicates;
import tools.devnull.trugger.selector.MethodSelector;

import java.lang.reflect.Method;
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
  private final boolean recursively;
  private final Predicate<? super Method> predicate;

  public TruggerMethodSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
    this.parameterTypes = null;
    this.recursively = false;
    this.predicate = null;
  }

  public TruggerMethodSelector(String name, MemberFindersRegistry registry,
                               Class[] parameterTypes, boolean recursively,
                               Predicate<? super Method> predicate) {
    this.name = name;
    this.registry = registry;
    this.parameterTypes = parameterTypes;
    this.recursively = recursively;
    this.predicate = predicate;
  }

  public MethodSelector withParameters(Class<?>... parameterTypes) {
    return new TruggerMethodSelector(name, registry, parameterTypes,
        recursively, predicate);
  }

  public MethodSelector withoutParameters() {
    return withParameters();
  }

  public MethodSelector deep() {
    return new TruggerMethodSelector(name, registry, parameterTypes,
        true, predicate);
  }

  @Override
  public MethodSelector filter(Predicate<? super Method> predicate) {
    return new TruggerMethodSelector(name, registry, parameterTypes,
        recursively, predicate);
  }

  public Method in(Object target) {
    if (parameterTypes != null) {
      return new MemberSelector<>(registry.methodFinder(name, parameterTypes),
          predicate, recursively).in(target);
    }
    MembersSelector<Method> selector = new MembersSelector<>(
        registry.methodsFinder(), predicate, recursively);
    return selector.in(target).stream()
        .filter(ReflectionPredicates.named(name))
        .findAny().orElse(null);
  }

}
