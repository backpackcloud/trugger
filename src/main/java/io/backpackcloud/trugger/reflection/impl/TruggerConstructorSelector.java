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

import io.backpackcloud.trugger.SelectionResult;
import io.backpackcloud.trugger.reflection.ConstructorSelector;
import io.backpackcloud.trugger.reflection.ReflectionException;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation for the constructor selector.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class TruggerConstructorSelector implements ConstructorSelector {

  private final Predicate<? super Constructor<?>> predicate;
  private final Class[] parameterTypes;
  private final MemberFindersRegistry registry;

  public TruggerConstructorSelector(MemberFindersRegistry registry) {
    this.registry = registry;
    this.predicate = null;
    this.parameterTypes = null;
  }

  public TruggerConstructorSelector(MemberFindersRegistry registry,
                                    Predicate<? super Constructor<?>> predicate,
                                    Class[] parameterTypes) {
    this.predicate = predicate;
    this.parameterTypes = parameterTypes;
    this.registry = registry;
  }

  public ConstructorSelector filter(
      Predicate<? super Constructor<?>> predicate) {
    return new TruggerConstructorSelector(registry, predicate, parameterTypes);
  }

  public ConstructorSelector withParameters(Class<?>... parameterTypes) {
    return new TruggerConstructorSelector(registry, predicate, parameterTypes);
  }

  public ConstructorSelector withoutParameters() {
    return withParameters();
  }

  @Override
  public SelectionResult<Constructor<?>> from(Object target) throws ReflectionException {
    if (parameterTypes != null) {
      return new MemberSelector(registry.constructorFinder(parameterTypes), predicate).selectFrom(target);
    }
    List<Constructor<?>> constructors =
        new MembersSelector<>(registry.constructorsFinder()).selectFrom(target);
    if (predicate != null) {
      return new SelectionResult(target, constructors.stream()
          .filter(predicate)
          .findFirst()
          .orElse(null));
    } else if (constructors.size() > 1) {
      throw new ReflectionException("More than one constructor found for " +
          target.getClass());
    } else {
      return new SelectionResult(target, constructors.iterator().next());
    }
  }

}
