/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.bind.impl;

import java.util.Collection;

import net.sf.trugger.Resolver;
import net.sf.trugger.bind.BindSelector;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementSpecifier;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.selector.FieldSelector;
import net.sf.trugger.selector.FieldSpecifier;
import net.sf.trugger.selector.FieldsSelector;

/**
 * A class for binding resolved values.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ResolverBinder implements BindSelector {

  private final Resolver<Object, Element> resolver;
  private final Collection<BindApplier> binds;

  /**
   * @param resolver
   *          the resolver to use.
   * @param binds
   *          the collection of binds for update.
   */
  public ResolverBinder(Resolver<Object, Element> resolver, Collection<BindApplier> binds) {
    this.resolver = resolver;
    this.binds = binds;
  }

  public FieldSpecifier toField(String fieldName) {
    FieldSelector selector = Reflection.reflect().field(fieldName).nonFinal().recursively();
    binds.add(new BindableElementBindApplier(selector, resolver));
    return selector;
  }

  public FieldSpecifier toFields() {
    FieldsSelector selector = Reflection.reflect().fields().nonFinal().recursively();
    binds.add(new BindableElementsBindApplier(selector, resolver));
    return selector;
  }

  public ElementSpecifier toElements() {
    ElementsSelector selector = Elements.elements();
    binds.add(new BindableElementsBindApplier(selector.forBind(), resolver));
    return selector;
  }

  public ElementSpecifier toElement(String name) {
    ElementSelector selector = Elements.element(name);
    binds.add(new BindableElementBindApplier(selector.forBind(), resolver));
    return selector;
  }

}
