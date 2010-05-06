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
package net.sf.trugger.message;

import java.util.ResourceBundle;

/**
 * Interface that defines a component that resolves messages based on a
 * reference.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public interface MessageResolver {
  
  /**
   * @param reference
   *          the reference for getting the message.
   * @param bundle
   *          the resource bundle used for internationalization.
   * @return a summary to the message based on the given context.
   */
  String getSummary(Object reference, ResourceBundle bundle);
  
  /**
   * @param reference
   *          the reference for getting the message.
   * @param bundle
   *          the resource bundle used for internationalization.
   * @return a detail to the message based on the given context.
   */
  String getDetail(Object reference, ResourceBundle bundle);
  
}
