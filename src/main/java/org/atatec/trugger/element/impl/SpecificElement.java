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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.element.Element;

/**
 * A class that represents a {@link Element#isSpecific() specific} element.
 *
 * @author Marcelo Guimarães
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
  public Object get() throws HandlingException {
    return in(target).get();
  }

  @Override
  public void set(Object value) throws HandlingException {
    in(target).set(value);
  }

}
