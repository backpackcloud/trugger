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
package net.sf.trugger.element.impl;

import net.sf.trugger.HandlingException;
import net.sf.trugger.element.Element;

/**
 * A class that represents a {@link Element#isSpecific() specific} element.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class SpecificElement extends DecoratedElement implements Element {

  protected final Object target;

  /**
   * @param element
   *          the specific element.
   * @param target
   *          the target for this specific element.
   */
  public SpecificElement(Element element, Object target) {
    super(element);
    this.target = target;
  }

  @Override
  public boolean isSpecific() {
    return true;
  }

  public <E> E target() {
    return (E) target;
  }

  @Override
  public Object value() throws HandlingException {
    return in(target).value();
  }

  @Override
  public void value(Object value) throws HandlingException {
    in(target).value(value);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((element == null) ? 0 : element.hashCode());
    result = prime * result + ((target == null) ? 0 : target.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SpecificElement other = (SpecificElement) obj;
    if (element == null) {
      if (other.element != null) {
        return false;
      }
    } else if (!element.equals(other.element)) {
      return false;
    }
    if (target == null) {
      if (other.target != null) {
        return false;
      }
    }
    return target == other.target;
  }

}
