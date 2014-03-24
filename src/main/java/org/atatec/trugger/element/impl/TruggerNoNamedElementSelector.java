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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.selector.ElementSelector;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Marcelo Guimarães
 */
public class TruggerNoNamedElementSelector implements ElementSelector {

  private final Predicate<? super Element> predicate;
  private final Finder<Element> finder;

  public TruggerNoNamedElementSelector(Finder<Element> finder) {
    this.finder = finder;
    this.predicate = null;
  }

  public TruggerNoNamedElementSelector(Finder<Element> finder,
                                       Predicate<? super Element> predicate) {
    this.predicate = predicate;
    this.finder = finder;
  }

  @Override
  public ElementSelector filter(Predicate<? super Element> predicate) {
    return new TruggerNoNamedElementSelector(finder, predicate);
  }

  @Override
  public Element in(Object target) {
    Set<Element> elements = finder.findAll().in(target);
    if (elements.isEmpty()) {
      return null;
    }
    if (predicate == null) {
      return elements.iterator().next();
    }
    return elements.stream()
        .filter(predicate)
        .findAny().orElse(null);
  }
}
