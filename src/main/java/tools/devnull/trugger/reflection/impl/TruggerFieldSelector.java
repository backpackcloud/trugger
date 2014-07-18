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

import tools.devnull.trugger.selector.FieldSelector;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * A default implementation for the field selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerFieldSelector implements FieldSelector {

  private final String name;
  private final MemberFindersRegistry registry;
  private final boolean recursively;
  private final Predicate<? super Field> predicate;

  public TruggerFieldSelector(String name, MemberFindersRegistry registry) {
    this.name = name;
    this.registry = registry;
    this.recursively = false;
    this.predicate = null;
  }

  public TruggerFieldSelector(String name, MemberFindersRegistry registry,
                              Predicate<? super Field> predicate,
                              boolean recursively) {
    this.name = name;
    this.registry = registry;
    this.recursively = recursively;
    this.predicate = predicate;
  }

  public FieldSelector filter(Predicate<? super Field> predicate) {
    return new TruggerFieldSelector(name, registry, predicate, recursively);
  }

  public FieldSelector deep() {
    return new TruggerFieldSelector(name, registry, predicate, true);
  }

  public Field in(Object target) {
    return new MemberSelector<>(
        registry.fieldFinder(name), predicate, recursively).in(target);
  }

}
