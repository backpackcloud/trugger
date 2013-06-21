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
package org.atatec.trugger.property.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.TruggerElementSelector;
import org.atatec.trugger.element.impl.TruggerElementsSelector;
import org.atatec.trugger.property.PropertyFactory;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;

/**
 * The default PropertyFactory implementation.
 *
 * @author Marcelo Guimarães
 */
public class TruggerPropertyFactory implements PropertyFactory {

  private Finder<Element> finder = new TruggerPropertyFinder();

  /**
   * Returns a new {@link TruggerElementSelector}.
   */
  public ElementSelector createPropertySelector(String name) {
    return new TruggerElementSelector(name, finder);
  }

  /**
   * Returns a new {@link TruggerElementsSelector}.
   */
  public ElementsSelector createPropertiesSelector() {
    return new TruggerElementsSelector(finder);
  }
}
