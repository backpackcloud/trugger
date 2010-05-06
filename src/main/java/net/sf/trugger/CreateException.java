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
package net.sf.trugger;

/**
 * This exception indicates an error occurred in an object instantiation.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class CreateException extends TruggerException {

  private static final long serialVersionUID = 1676274394394669394L;

  /**
   * Creates a new CreateException.
   */
  public CreateException() {
    super();
  }

  /**
   * Creates a new CreateException with the given message.
   *
   * @param message
   *          the exception message
   */
  public CreateException(String message) {
    super(message);
  }

  /**
   * Creates a new CreateException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public CreateException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new CreateException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public CreateException(Throwable cause) {
    super(cause);
  }

}
