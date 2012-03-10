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
package net.sf.trugger.property.impl;

import net.sf.trugger.Finder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.TruggerElementSelector;
import net.sf.trugger.element.impl.TruggerElementsSelector;
import net.sf.trugger.property.PropertyFactory;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementsSelector;

/**
 * The default PropertyFactory implementation.
 *
 * @author Marcelo Varella Barca Guimarães
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
