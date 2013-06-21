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
package org.atatec.trugger.validation;

import org.atatec.trugger.annotation.Reference;

/**
 * Exception thrown to indicate that a {@link Reference} is invalid.
 *
 * @author Marcelo Guimarães
 * @since 2.4
 */
public class InvalidReferenceException extends ValidationException {

  private static final long serialVersionUID = 1114104269367485898L;

  /**
   * Creates a new InvalidReferenceException.
   */
  public InvalidReferenceException() {
    super();
  }

  /**
   * Creates a new InvalidReferenceException with the given message.
   *
   * @param message
   *          the exception message
   */
  public InvalidReferenceException(String message) {
    super(message);
  }

  /**
   * Creates a new InvalidReferenceException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public InvalidReferenceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new InvalidReferenceException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public InvalidReferenceException(Throwable cause) {
    super(cause);
  }

}
