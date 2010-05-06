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
package net.sf.trugger.scan;

import net.sf.trugger.TruggerException;

/**
 * Exception thrown to indicate an error while searching for classes.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class ClassScanningException extends TruggerException {

  private static final long serialVersionUID = 1969430738922732416L;

  /**
   * Creates a new ClassFindingException.
   */
  public ClassScanningException() {
    super();
  }

  /**
   * Creates a new ClassFindingException with the given message.
   *
   * @param message
   *            the message of the exception
   */
  public ClassScanningException(String message) {
    super(message);
  }

  /**
   * Creates a new ClassFindingException with a formatted message.
   *
   * @param format
   *            the format of the message
   * @param args
   *            the arguments to format the message
   */
  public ClassScanningException(String format, Object... args) {
    super(String.format(format, args));
  }

  /**
   * Creates a new ClassFindingException with the given cause and message
   *
   * @param cause
   *            the cause of the exception
   * @param format
   *            the format of the message
   * @param args
   *            the arguments to format the message
   */
  public ClassScanningException(Throwable cause, String format, Object... args) {
    super(String.format(format, args), cause);
  }

  /**
   * Creates a new ClassFindingException with the given cause and message
   *
   * @param message
   *            the message of the exception
   * @param cause
   *            the cause of the exception
   */
  public ClassScanningException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new ClassFindingException with the given cause.
   *
   * @param cause
   *            the cause of the exception
   */
  public ClassScanningException(Throwable cause) {
    super(cause);
  }

}
