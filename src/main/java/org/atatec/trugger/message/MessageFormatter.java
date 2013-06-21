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
package org.atatec.trugger.message;

import java.util.ResourceBundle;

import org.atatec.trugger.element.Element;

/**
 * Interface that defines a formatter to a message, this interface can be used
 * with a {@link MessageCreator} to create formatted messages.
 * 
 * @author Marcelo Guimarães
 * @since 2.1
 */
public interface MessageFormatter {
  
  /**
   * Formats the message based on an analysis context.
   * 
   * @param message
   *          the message content
   * @param bundle
   *          the resource bundle used for resolving the message
   * @param targetElement
   *          the element of the target
   * @param contextObject
   *          the context object
   * @param target
   *          the context target
   * @return the formatted message
   */
  String format(String message, ResourceBundle bundle, Element targetElement, Object contextObject, Object target);
}
