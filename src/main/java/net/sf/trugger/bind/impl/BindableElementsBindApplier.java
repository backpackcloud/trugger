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

import java.util.Set;

import net.sf.trugger.Resolver;
import net.sf.trugger.Result;
import net.sf.trugger.Transformer;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;

/**
 * BindApplier for a selection of elements.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public final class BindableElementsBindApplier extends AbstractBindApplier implements BindApplier {
  
  private final Result selector;
  
  /**
   * Creates an instance for binding a value.
   */
  BindableElementsBindApplier(Result selector, Object value) {
    super(value);
    this.selector = selector;
  }
  
  /**
   * Creates an instance for binding a resolved value.
   */
  BindableElementsBindApplier(Result selector, Resolver<Object, Element> resolver) {
    super(resolver);
    this.selector = selector;
  }
  
  public void applyBind(Object target) {
    Set<?> elements = (Set<?>) selector.in(target);
    Transformer<BindableElement, Object> transformer = new BindableElementTransformer(target);
    for (Object element : elements) {
      applyBindInElement(transformer.transform(element));
    }
  }
  
}
