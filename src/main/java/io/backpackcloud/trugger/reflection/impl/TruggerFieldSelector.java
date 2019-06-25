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

import io.backpackcloud.trugger.reflection.FieldSelector;
import io.backpackcloud.trugger.reflection.ReflectedField;
import io.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A default implementation for the field selector.
 *
 * @author Marcelo Guimaraes
 */
public class TruggerFieldSelector implements FieldSelector {

  private final String name;
  private final MemberFindersRegistry registry;
  private final Predicate<? super Field> predicate;
  private final Function<Class, Iterable<Class>> function;

  public TruggerFieldSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
    this.function = Collections::singletonList;
    this.predicate = null;
  }

  public TruggerFieldSelector(String name, MemberFindersRegistry registry,
                              Predicate<? super Field> predicate,
                              Function<Class, Iterable<Class>> function) {
    this.name = name;
    this.registry = registry;
    this.predicate = predicate;
    this.function = function;
  }

  public FieldSelector filter(Predicate<? super Field> predicate) {
    return new TruggerFieldSelector(this.name, this.registry, predicate, this.function);
  }

  public FieldSelector deep() {
    return new TruggerFieldSelector(this.name, this.registry, this.predicate, Reflection::hierarchyOf);
  }

  public Optional<ReflectedField> from(Object target) {
    return new MemberSelector<>(registry.fieldFinder(name), predicate, function)
        .selectFrom(target)
        .map(field -> new ReflectedField(field, target));
  }

}
