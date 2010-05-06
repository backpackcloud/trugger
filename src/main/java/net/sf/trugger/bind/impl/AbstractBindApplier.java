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

import net.sf.trugger.Resolver;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.util.Utils;

/**
 * Base class for applying binds into elements.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class AbstractBindApplier implements BindApplier {

  private Object value;
  private Resolver<Object, Element> resolver;

  /**
   * @param resolver
   *          the component to resolve the value for binding.
   */
  protected AbstractBindApplier(Resolver<Object, Element> resolver) {
    this.resolver = resolver;
  }

  /**
   * @param value
   *          the value for binding.
   */
  protected AbstractBindApplier(Object value) {
    this.value = value;
  }

  /**
   * Applies the bind using the resolver or the value itself into the given
   * element.
   *
   * @param element
   *          the element for receiving the value.
   */
  protected void applyBindInElement(BindableElement element) {
    Object bindValue = value;
    if ((value == null) && (resolver != null)) {
      bindValue = resolver.resolve(element);
    }
    if ((bindValue != null) && Utils.areAssignable(element.type(), bindValue.getClass())) {
      element.bind(bindValue);
    }
  }

}
