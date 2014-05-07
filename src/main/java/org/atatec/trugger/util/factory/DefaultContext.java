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

package org.atatec.trugger.util.factory;

import org.atatec.trugger.PredicateMapper;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DefaultContext implements Context {

  private List<Entry> entries;

  public DefaultContext() {
    this.entries = new ArrayList<>();
  }

  @Override
  public PredicateMapper<Parameter, Context> use(Object object) {
    return use((parameter) -> object);
  }

  @Override
  public PredicateMapper<Parameter, Context> use(Supplier supplier) {
    return use(parameter -> supplier.get());
  }

  @Override
  public PredicateMapper<Parameter, Context> use(Function<Parameter, Object> function) {
    return (predicate) -> {
      entries.add(new Entry(function, predicate));
      return DefaultContext.this;
    };
  }

  @Override
  public Object resolve(Parameter parameter) {
    for (Entry entry : entries) {
      if (entry.predicate.test(parameter)) {
        return entry.function.apply(parameter);
      }
    }
    throw new UnresolvableValueException();
  }

  private static class Entry {

    private final Function<Parameter, Object> function;
    private final Predicate<Parameter> predicate;

    private Entry(Function<Parameter, Object> supplier,
                  Predicate<Parameter> predicate) {
      this.function = supplier;
      this.predicate = predicate;
    }

  }

}
