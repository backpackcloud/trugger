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
package org.atatec.trugger.element.impl;

import static org.atatec.trugger.iteration.Iteration.selectFrom;

import java.util.Set;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;


/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class TruggerNoNamedElementSelector extends TruggerElementSelector {

  public TruggerNoNamedElementSelector(Finder<Element> finder) {
    super(null, finder);
  }

  /* (non-Javadoc)
   * @see org.atatec.trugger.Result#in(java.lang.Object)
   */
  @Override
  public Element in(Object target) {
    Set<Element> elements = finder().findAll().in(target);
    return selectFrom(elements).oneThat(builder().predicate());
  }

}
