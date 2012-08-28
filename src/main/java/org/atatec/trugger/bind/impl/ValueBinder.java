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

import org.atatec.trugger.bind.BindSelector;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementSpecifier;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.selector.FieldSpecifier;
import org.atatec.trugger.selector.FieldsSelector;

import java.util.Collection;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.reflection.Reflection.fields;

/**
 * A class for binding values.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ValueBinder implements BindSelector {

  private final Object value;
  private final Collection<BindApplier> binds;

  /**
   * @param value
   *          the value to bind.
   * @param binds
   *          the collection of binds for update.
   */
  public ValueBinder(Object value, Collection<BindApplier> binds) {
    this.value = value;
    this.binds = binds;
  }

  public FieldSelector toField(String fieldName) {
    FieldSelector selector = field(fieldName).nonFinal().recursively();
    binds.add(new BindableElementBindApplier(selector, value));
    return selector;
  }

  public FieldsSelector toFields() {
    FieldsSelector selector = fields().nonFinal().recursively();
    binds.add(new BindableElementsBindApplier(selector, value));
    return selector;
  }

  public FieldSpecifier toField() {
    FieldSelector selector = field().nonFinal().recursively();
    binds.add(new BindableElementBindApplier(selector, value));
    return selector;
  }

  public ElementsSelector toElements() {
    ElementsSelector selector = elements();
    binds.add(new BindableElementsBindApplier(selector.forBind(), value));
    return selector;
  }

  public ElementSelector toElement(String name) {
    ElementSelector selector = element(name);
    binds.add(new BindableElementBindApplier(selector.forBind(), value));
    return selector;
  }

  public ElementSpecifier toElement() {
    ElementSelector selector = element();
    binds.add(new BindableElementBindApplier(selector.forBind(), value));
    return selector;
  }

}
