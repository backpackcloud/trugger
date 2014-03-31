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
  public Context put(Object object, Predicate<Parameter> predicate) {
    return put((parameter) -> object, predicate);
  }

  @Override
  public Context put(Supplier supplier, Predicate<Parameter> predicate) {
    return put(parameter -> supplier.get(), predicate);
  }

  @Override
  public Context put(Function<Parameter, Object> function,
                     Predicate<Parameter> predicate) {
    entries.add(new Entry(function, predicate));
    return this;
  }

  @Override
  public Object resolve(Parameter parameter) {
    for (Entry entry : entries) {
      if (entry.predicate.test(parameter)) {
        return entry.function.apply(parameter);
      }
    }
    return null;
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
