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
package net.sf.trugger.iteration;

import net.sf.trugger.TruggerException;

/**
 * Exception thrown to indicate an error while searching in a collection.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class SearchException extends TruggerException {

  private static final long serialVersionUID = -2028321962725485706L;

  /**
   * Creates a new SearchException.
   */
  public SearchException() {
    super();
  }

  /**
   * Creates a new SearchException with the given message.
   *
   * @param message
   *          the exception message
   */
  public SearchException(String message) {
    super(message);
  }

  /**
   * Creates a new SearchException with the given cause and message
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public SearchException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new SearchException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public SearchException(Throwable cause) {
    super(cause);
  }

}
