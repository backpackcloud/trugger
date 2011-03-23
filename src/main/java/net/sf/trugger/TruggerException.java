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
package net.sf.trugger;

/**
 * The root exception for the entire framework.
 *
 * @author Marcelo Varella Barca Guimarães.
 */
public class TruggerException extends RuntimeException {

  private static final long serialVersionUID = 7147780283509270147L;

  /**
   * Creates a new TruggerException.
   */
  public TruggerException() {
    super();
  }

  /**
   * Creates a new TruggerException with the given message.
   *
   * @param message
   *          the exception message
   */
  public TruggerException(String message) {
    super(message);
  }

  /**
   * Creates a new TruggerException with the given cause and message.
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public TruggerException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new TruggerException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public TruggerException(Throwable cause) {
    super(cause);
  }

}
