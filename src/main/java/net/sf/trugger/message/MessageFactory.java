/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.message;

import java.util.ResourceBundle;

/**
 * A factory for creating message components.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public interface MessageFactory {
  
  /**
   * Creates a new component for creating messages.
   * 
   * @param formatter
   *          a component for formatting the message.
   * @param resolver
   *          a component for resolving the message.
   * @param bundle
   *          the resource bundle for resolving the message.
   * @return the created component.
   */
  MessageCreator createMessageCreator(MessageFormatter formatter, MessageResolver resolver, ResourceBundle bundle);
  
  /**
   * Creates a new component for resolving messages.
   * 
   * @return the created component.
   */
  MessageResolver createMessageResolver();
  
  /**
   * Creates a new component for formatting messages.
   * 
   * @return the created component.
   */
  MessageFormatter createMessageFormatter();
  
  /**
   * Creates a new message.
   * 
   * @param summary
   *          the short description of the message.
   * @param detail
   *          the full description of the message. It may be <code>null</code>.
   * @return the created message.
   */
  Message createMessage(String summary, String detail);
  
  /**
   * Creates a new message.
   * 
   * @param description
   *          the description of the message. It will be both summary and detail
   * @return the created message.
   */
  Message createMessage(String description);
  
}
