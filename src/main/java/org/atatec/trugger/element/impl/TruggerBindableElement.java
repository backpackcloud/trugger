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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.HashBuilder;

/**
 * An {@link BindableElement} composed by an {@link Element}.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerBindableElement extends DecoratedElement implements BindableElement {

  private final Object target;

  /**
   * Creates a new FieldElement based on the given field and target.
   */
  public TruggerBindableElement(Element element, Object target) {
	  super(element);
    this.target = target;
  }

  public void bind(Object value) {
    value(value);
  }

  @Override
  public boolean isSpecific() {
    return true;
  }

  @Override
  public <E> E target() {
    return (E) target;
  }

  @Override
  public <E> E value() throws HandlingException {
    return (E) element.in(target).value();
  }

  @Override
  public void value(Object value) throws HandlingException {
    element.in(target).value(value);
  }

  @Override
  public int hashCode() {
    return new HashBuilder(super.hashCode()).add(target).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof TruggerBindableElement) {
      TruggerBindableElement other = (TruggerBindableElement) obj;
      if (target == null) {
        if (other.target != null) {
          return false;
        }
      } else if (target != other.target) {
        return false;
      }
      if (element == null) {
        if (other.element != null) {
          return false;
        }
      } else if (!element.equals(other.element)) {
        return false;
      }
      return true;
    }
    return false;
  }

}
