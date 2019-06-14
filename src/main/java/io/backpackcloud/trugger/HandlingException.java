/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger;

/**
 * Exception thrown when an error occurs while handling a value.
 *
 * @author Marcelo "Ataxexe" Guimarães
 * @since 2.0
 */
public class HandlingException extends TruggerException {

  private static final long serialVersionUID = 5344663069662709644L;

  /**
   * Creates a new HandlingException.
   */
  public HandlingException() {
    super();
  }

  /**
   * Creates a new HandlingException with the given message.
   *
   * @param message
   *          the exception message
   */
  public HandlingException(String message) {
    super(message);
  }

  /**
   * Creates a new HandlingException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public HandlingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new HandlingException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public HandlingException(Throwable cause) {
    super(cause);
  }

}
