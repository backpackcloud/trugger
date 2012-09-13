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
package org.atatec.trugger.iteration;

import org.atatec.trugger.TruggerException;

/**
 * Exception thrown to indicate that a predicate given to search for an unique element
 * matches two or more elements..
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class NonUniqueMatchException extends TruggerException {

  private static final long serialVersionUID = -2028321962725485706L;

  /** Creates a new NonUniqueMatchException. */
  public NonUniqueMatchException() {
    super();
  }

  /**
   * Creates a new NonUniqueMatchException with the given message.
   *
   * @param message the exception message
   */
  public NonUniqueMatchException(String message) {
    super(message);
  }

  /**
   * Creates a new NonUniqueMatchException with the given cause and message
   *
   * @param message the exception message
   * @param cause   the exception cause
   */
  public NonUniqueMatchException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new NonUniqueMatchException with the given cause.
   *
   * @param cause the exception cause
   */
  public NonUniqueMatchException(Throwable cause) {
    super(cause);
  }

}
