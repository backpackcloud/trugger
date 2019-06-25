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

import io.backpackcloud.trugger.reflection.FieldsSelector;
import io.backpackcloud.trugger.reflection.ReflectedField;
import io.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A default implementation for the fields selector.
 *
 * @author Marcelo Guimaraes
 */
public class TruggerFieldsSelector implements FieldsSelector {

  private final MembersFinder<Field> finder;
  private final Predicate<? super Field> predicate;
  private final Function<Class, Iterable<Class>> function;

  public TruggerFieldsSelector(MembersFinder<Field> finder) {
    this.finder = finder;
    this.predicate = null;
    this.function = Collections::singletonList;
  }

  public TruggerFieldsSelector(MembersFinder<Field> finder,
                               Predicate<? super Field> predicate,
                               Function<Class, Iterable<Class>> function) {
    this.finder = finder;
    this.predicate = predicate;
    this.function = function;
  }

  @Override
  public FieldsSelector filter(Predicate<? super Field> predicate) {
    return new TruggerFieldsSelector(this.finder, predicate, this.function);
  }

  @Override
  public FieldsSelector deep() {
    return new TruggerFieldsSelector(this.finder, this.predicate, Reflection::hierarchyOf);
  }

  public List<ReflectedField> from(Object target) {
    return new MembersSelector<>(finder, predicate, function)
        .selectFrom(target)
        .stream()
        .map(field -> new ReflectedField(field, target))
        .collect(Collectors.toList());
  }

}
