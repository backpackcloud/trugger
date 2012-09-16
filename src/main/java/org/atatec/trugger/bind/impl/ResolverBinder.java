/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
import org.atatec.trugger.bind.ResolverBindSelector;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.selector.FieldsSelector;

import java.util.Collection;

/**
 * A class for binding resolved values.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ResolverBinder implements ResolverBindSelector {

  private final Resolver<Object, Element> resolver;
  private final Collection<BindApplier> binds;
  private final Binder binder;

  public ResolverBinder(Resolver<Object, Element> resolver, Collection<BindApplier> binds, Binder binder) {
    this.resolver = resolver;
    this.binds = binds;
    this.binder = binder;
  }

  @Override
  public Binder in(FieldSelector selector) {
    binds.add(new BindableElementBindApplier(selector.nonFinal().recursively(), resolver));
    return binder;
  }

  @Override
  public Binder in(FieldsSelector selector) {
    binds.add(new BindableElementsBindApplier(selector.nonFinal().recursively(), resolver));
    return binder;
  }

  @Override
  public Binder in(ElementSelector selector) {
    binds.add(new BindableElementBindApplier(selector.forBind(), resolver));
    return binder;
  }

  @Override
  public Binder in(ElementsSelector selector) {
    binds.add(new BindableElementsBindApplier(selector.forBind(), resolver));
    return binder;
  }

}
