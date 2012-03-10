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

import net.sf.trugger.TruggerException;

/**
 * Exception thrown when an error occurs while validating an object.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.4
 */
public class ValidationException extends TruggerException {

  private static final long serialVersionUID = 7810902495496936478L;

  /**
   * Creates a new ValidationException.
   */
  public ValidationException() {
    super();
  }

  /**
   * Creates a new ValidationException with the given message.
   *
   * @param message
   *          the exception message
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * Creates a new ValidationException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new ValidationException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public ValidationException(Throwable cause) {
    super(cause);
  }

}
