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
package net.sf.trugger.message;

/**
 * Identifies the parts of a {@link Message}.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public enum MessagePart {
  /**
   * Indicates a {@link Message#getSummary() summary}.
   */
  SUMMARY {
    
    @Override
    public String getPart(Message message) {
      return message.getSummary();
    }
  },
  /**
   * Indicates a {@link Message#getDetail() detail}.
   */
  DETAIL {
    
    @Override
    public String getPart(Message message) {
      return message.getDetail();
    }
  };
  
  /**
   * Returns the part of the message identified by this enumeration.
   * 
   * @param message
   *          the message object for retrieve the part.
   * @return the part of the message identified by this enumeration.
   */
  public abstract String getPart(Message message);
}
