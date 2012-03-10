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
package net.sf.trugger.validation;

import java.util.List;

import net.sf.trugger.element.Element;
import net.sf.trugger.message.Message;
import net.sf.trugger.message.MessagePart;

/**
 * Interface that defines a element that has an invalid value.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface InvalidElement extends Element {

  /**
   * @return the full path of this element if it is a nested property. Else,
   *         returns the {@link #name() name}.
   */
  String path();

  /**
   * Returns the invalid value.
   * <p>
   * Note that this method may not return the same value as {@link #value()}
   * because the element value may change.
   *
   * @return the invalid value of the element
   */
  Object invalidValue();

  /**
   * Returns a collection of the messages for this invalid element.
   * <p>
   * This method should not return a <code>null</code> value.
   *
   * @return the messages for this invalid element.
   */
  List<Message> messages();

  /**
   * Returns the {@link #messages()} joined by the
   * <code>messageSeparator</code>.
   *
   * @param separator
   *          the separator to join the messages.
   * @param type
   *          the type of the message to include.
   * @return the joined message.
   */
  String joinMessages(String separator, MessagePart type);

}
