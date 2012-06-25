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
package org.atatec.trugger.message;

import java.util.ResourceBundle;

import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.util.Null;

/**
 * An utility class for handling messages.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class Messages {
  
  private static final MessageFactory factory;
  
  static {
    factory = ImplementationLoader.getInstance().get(MessageFactory.class);
  }
  
  /**
   * @see MessageFactory#createMessage(String, String)
   */
  public static Message newMessage(String summary, String detail) {
    return factory.createMessage(summary, detail);
  }
  
  /**
   * @see MessageFactory#createMessage(String)
   */
  public static Message newMessage(String summary) {
    return factory.createMessage(summary);
  }
  
  /**
   * @see MessageFactory#createMessageCreator(MessageFormatter, MessageResolver,
   *      ResourceBundle)
   */
  public static MessageCreator newMessageCreator(MessageFormatter formatter, MessageResolver resolver,
      ResourceBundle bundle) {
    return factory.createMessageCreator(formatter, resolver, bundle);
  }
  
  /**
   * Uses a {@link Null#NULL_BUNDLE null bundle} for creating a message
   * creator..
   * 
   * @see MessageFactory#createMessageCreator(MessageFormatter, MessageResolver,
   *      ResourceBundle)
   */
  public static MessageCreator newMessageCreator(MessageFormatter formatter, MessageResolver resolver) {
    return factory.createMessageCreator(formatter, resolver, Null.NULL_BUNDLE);
  }
  
  /**
   * Uses the {@link #newMessageFormatter()} and {@link #newMessageResolver()}
   * plus the given bundle for creating a message creator.
   * 
   * @see MessageFactory#createMessageCreator(MessageFormatter, MessageResolver,
   *      ResourceBundle)
   */
  public static MessageCreator createMessageCreator(ResourceBundle bundle) {
    return factory.createMessageCreator(newMessageFormatter(), newMessageResolver(), bundle);
  }
  
  /**
   * Creates a new message creator using the registered factory for passing the formatter and resolver.
   */
  public static MessageCreator createMessageCreator() {
    return factory.createMessageCreator(newMessageFormatter(), newMessageResolver(), Null.NULL_BUNDLE);
  }
  
  /**
   * @see MessageFactory#createMessageResolver()
   */
  public static MessageResolver newMessageResolver() {
    return factory.createMessageResolver();
  }
  
  /**
   * @see MessageFactory#createMessageFormatter()
   */
  public static MessageFormatter newMessageFormatter() {
    return factory.createMessageFormatter();
  }
  
}
