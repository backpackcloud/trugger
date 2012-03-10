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
import net.sf.trugger.element.impl.SpecificElement;
import net.sf.trugger.message.Message;
import net.sf.trugger.message.MessagePart;
import net.sf.trugger.util.HashBuilder;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.InvalidElement;

/**
 * This class represents an invalid element.
 *
 * @author Marcelo Varella Barca Guimarães
 */
final class TruggerInvalidElement extends SpecificElement implements InvalidElement {

  private final Object value;

  /** the messages associated with this property */
  private final Collection<Message> messages;

  /**
   * Creates a new invalid element.
   *
   * @param element
   *          the analyzed element
   * @param value
   *          the value of the property
   * @param target
   *          the target
   * @param messages
   *          the messages associated to this property
   */
  TruggerInvalidElement(Element element, Object value, Object target, Collection<Message> messages) {
    super(element, target);
    this.messages = messages;
    this.value = value;
  }

  public List<Message> messages() {
    return new ArrayList<Message>(this.messages);
  }

  public String joinMessages(String messageSeparator, MessagePart type) {
    StringBuilder builder = new StringBuilder(60);
    String part;
    for (Message message : messages) {
      part = type.getPart(message);
      if (!Utils.isEmpty(part)) {
        if (builder.length() > 0) {
          builder.append(messageSeparator);
        }
        builder.append(part);
      }
    }
    return builder.toString();
  }

  public Object invalidValue() {
    return value;
  }

  public String path() {
    return element.name();
  }

  @Override
  public int hashCode() {
    return new HashBuilder(super.hashCode()).add(messages).add(value).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TruggerInvalidElement other = (TruggerInvalidElement) obj;
    if (messages == null) {
      if (other.messages != null) {
        return false;
      }
    } else if (!messages.equals(other.messages)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

}
