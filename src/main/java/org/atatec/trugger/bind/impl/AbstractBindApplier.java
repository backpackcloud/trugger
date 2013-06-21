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
package org.atatec.trugger.bind.impl;

import org.atatec.trugger.Resolver;
import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.Utils;

/**
 * Base class for applying binds into elements.
 *
 * @author Marcelo Guimarães
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
  protected final void applyBindInElement(BindableElement element) {
    Object bindValue = value;
    if ((value == null) && (resolver != null)) {
      bindValue = resolver.resolve(element);
    }
    if ((bindValue == null) || Utils.areAssignable(element.type(), bindValue.getClass())) {
      element.bind(bindValue);
    }
  }

}
