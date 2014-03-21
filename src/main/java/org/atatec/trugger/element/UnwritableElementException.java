/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.element;

import org.atatec.trugger.HandlingException;

/**
 * Exception thrown when a code tries to change the value of a read-only
 * element.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public class UnwritableElementException extends HandlingException {

  private static final long serialVersionUID = 496021032195162319L;

  /**
   * Creates a new UnwritablePropertyException.
   */
  public UnwritableElementException() {
    super();
  }

  /**
   * Creates a new UnwritablePropertyException with the given message.
   *
   * @param message
   *          the exception message
   */
  public UnwritableElementException(String message) {
    super(message);
  }

  /**
   * Creates a new UnwritablePropertyException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public UnwritableElementException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new UnwritablePropertyException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public UnwritableElementException(Throwable cause) {
    super(cause);
  }
}
