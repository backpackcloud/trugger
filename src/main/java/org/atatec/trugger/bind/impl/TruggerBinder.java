/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.bind.impl;

import org.atatec.trugger.Resolver;
import org.atatec.trugger.bind.Binder;
import org.atatec.trugger.bind.PostBind;
import org.atatec.trugger.bind.ResolverBindSelector;
import org.atatec.trugger.bind.ValueBindSelector;
import org.atatec.trugger.element.Element;

import java.util.Collection;
import java.util.LinkedList;

import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.methods;

/**
 * A default implementation for binding operations.
 * <p>
 * The binds are applied in order they are registered with the
 * {@link #bind(Object)} and {@link #use(Resolver)} methods.
 * 
 * @author Marcelo Guimarães
 */
public final class TruggerBinder implements Binder {
  
  /**
   * Stores the binds in order.
   */
  private final Collection<BindApplier> binds = new LinkedList<BindApplier>();
  
  public ValueBindSelector bind(Object value) {
    return new ValueBinder(value, binds, this);
  }
  
  public ResolverBindSelector use(Resolver<Object, Element> resolver) {
    return new ResolverBinder(resolver, binds, this);
  }
  
  public <E> E applyIn(E object) {
    for (BindApplier bind : binds) {
      bind.applyBind(object);
    }
    invoke(
      methods().recursively()
        .annotatedWith(PostBind.class)
      .withoutParameters()
    ).in(object).withoutArgs();
    return object;
  }
  
}
