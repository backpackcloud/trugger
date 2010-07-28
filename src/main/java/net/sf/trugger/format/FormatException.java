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
package net.sf.trugger.format;

import net.sf.trugger.TruggerException;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class FormatException extends TruggerException {

  private static final long serialVersionUID = -2550746383618421929L;

  /**
   * Creates a new FormatException.
   */
  public FormatException() {
    super();
  }

  /**
   * Creates a new FormatException with the given message.
   *
   * @param message
   *          the exception message
   */
  public FormatException(String message) {
    super(message);
  }

  /**
   * Creates a new FormatException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public FormatException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new FormatException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public FormatException(Throwable cause) {
    super(cause);
  }

}