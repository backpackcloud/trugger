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
package org.atatec.trugger.message.impl;

import java.util.ResourceBundle;

import org.atatec.trugger.message.Message;
import org.atatec.trugger.message.MessageCreator;
import org.atatec.trugger.message.MessageFactory;
import org.atatec.trugger.message.MessageFormatter;
import org.atatec.trugger.message.MessageResolver;

/**
 * The default MessageFactory implementation.
 * 
 * @author Marcelo Guimarães.
 */
public class TruggerMessageFactory implements MessageFactory {
  
  /**
   * Returns a new {@link TruggerMessage}.
   */
  public Message createMessage(String summary, String detail) {
    return new TruggerMessage(summary, detail);
  }
  
  /**
   * Returns a new {@link TruggerMessage}.
   */
  public Message createMessage(String description) {
    return new TruggerMessage(description, description);
  }
  
  /**
   * Returns a new {@link TruggerMessageCreator}.
   */
  public MessageCreator createMessageCreator(MessageFormatter formatter, MessageResolver resolver, ResourceBundle bundle) {
    return new TruggerMessageCreator(resolver, formatter, bundle);
  }
  
  /**
   * Returns a new {@link TruggerMessageFormatter}.
   */
  public MessageFormatter createMessageFormatter() {
    return new TruggerMessageFormatter();
  }
  
  /**
   * Returns a new {@link TruggerMessageResolver}.
   */
  public MessageResolver createMessageResolver() {
    return new TruggerMessageResolver();
  }
  
}
