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

import java.util.function.Predicate;

/**
 * A default implementation for {@link ElementSelector}.
 *
 * @author Marcelo Guimarães
 */
public class TruggerElementSelector implements ElementSelector {

  private final Predicate<? super Element> predicate;
  private final Finder<Element> finder;
  private final String name;

  public TruggerElementSelector(String name, Finder<Element> finder) {
    this.name = name;
    this.finder = finder;
    this.predicate = null;
  }

  public TruggerElementSelector(String name, Finder<Element> finder,
                                Predicate<? super Element> predicate) {
    this.predicate = predicate;
    this.finder = finder;
    this.name = name;
  }

  public ElementSelector filter(Predicate<? super Element> predicate) {
    return new TruggerElementSelector(name, finder, predicate);
  }

  public Element in(Object target) {
    Element element = finder.find(name).in(target);
    if (element == null) {
      return null;
    }
    if (predicate != null) {
      return predicate.test(element) ? element : null;
    }
    return element;
  }

}
