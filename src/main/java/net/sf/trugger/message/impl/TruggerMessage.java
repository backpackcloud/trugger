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
package net.sf.trugger.message.impl;

import net.sf.trugger.message.Message;

/**
 * A default implementation for a message.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerMessage implements Message {
  
  private final String summary;
  
  private final String detail;
  
  /**
   * Creates a new message.
   * 
   * @param summary
   *          the message summary
   * @param detail
   *          the message detail
   */
  public TruggerMessage(String summary, String detail) {
    this.detail = detail;
    this.summary = summary;
  }
  
  public String getSummary() {
    return this.summary;
  }
  
  public String getDetail() {
    return this.detail;
  }
  
  @Override
  public String toString() {
    return summary;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.detail == null) ? 0 : this.detail.hashCode());
    result = prime * result + ((this.summary == null) ? 0 : this.summary.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TruggerMessage other = (TruggerMessage) obj;
    if (this.detail == null) {
      if (other.detail != null) {
        return false;
      }
    } else if (!this.detail.equals(other.detail)) {
      return false;
    }
    if (this.summary == null) {
      if (other.summary != null) {
        return false;
      }
    } else if (!this.summary.equals(other.summary)) {
      return false;
    }
    return true;
  }
  
}
