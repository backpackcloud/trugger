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
package net.sf.trugger.validation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.trugger.element.Element;
import net.sf.trugger.message.Message;
import net.sf.trugger.validation.InvalidElement;

/**
 * A class for holding the parameters of a validation.
 *
 * @author Marcelo Varella Barca Guimarães
 */
class ValidationParameter {

  /**
   * The element being validated
   */
  public final Element element;
  /**
   * The target
   */
  public final Object target;
  /**
   * The elements that are wrapped into an invalid element
   */
  public final Collection<InvalidElement> invalidElements;
  /**
   * The valid elements
   */
  public final Collection<Element> currentValidElements;
  /**
   * The invalid elements
   */
  public final Collection<Element> currentInvalidElements;
  /**
   * The messages associated with the current element.
   */
  public final List<Message> messages = new ArrayList<Message>();

  ValidationParameter(Object target, Element property, Collection<Element> currentInvalidElements,
      Collection<Element> currentValidElements, Collection<InvalidElement> invalidElements) {
    this.target = target;
    this.element = property;
    this.currentInvalidElements = currentInvalidElements;
    this.currentValidElements = currentValidElements;
    this.invalidElements = invalidElements;
  }

}
