/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.element;

import tools.devnull.trugger.HandlingException;

/**
 * Exception thrown when a code tries to get the value of a write-only element.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public class UnreadableElementException extends HandlingException {

  private static final long serialVersionUID = -1058389236167393390L;

  /**
   * Creates a new UnreadablePropertyException.
   */
  public UnreadableElementException() {
    super();
  }

  /**
   * Creates a new UnreadablePropertyException with the given message.
   *
   * @param message
   *          the exception message
   */
  public UnreadableElementException(String message) {
    super(message);
  }

  /**
   * Creates a new UnreadablePropertyException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public UnreadableElementException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new UnreadablePropertyException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public UnreadableElementException(Throwable cause) {
    super(cause);
  }
}
