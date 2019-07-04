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

package io.backpackcloud.trugger.element.impl;

import io.backpackcloud.trugger.element.ElementFactory;
import io.backpackcloud.trugger.element.ElementSelection;
import io.backpackcloud.trugger.element.ElementSelector;
import io.backpackcloud.trugger.element.ElementsSelector;

public class TruggerElementSelection implements ElementSelection {

  private final ElementFactory factory;

  public TruggerElementSelection(ElementFactory factory) {
    this.factory = factory;
  }

  @Override
  public ElementSelector element(String name) {
    return factory.createElementSelector(name);
  }

  @Override
  public ElementsSelector elements() {
    return factory.createElementsSelector();
  }

  @Override
  public ElementSelector element() {
    return new SingleElementSelector();
  }

}
