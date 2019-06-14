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
package io.backpackcloud.trugger.reflection;

import io.backpackcloud.trugger.TruggerException;

/**
 * Exception thrown when an error occurs in a Reflection operation.
 * <p>
 * This exception should be used only for encapsulation of another exceptions.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class ReflectionException extends TruggerException {

  private static final long serialVersionUID = 4050159339450036003L;

  /**
   * Creates a new ReflectionException with the given cause.
   *
   * @param cause
   *          the exception cause
   */
  public ReflectionException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new ReflectionException with the given cause and the specified
   * message.
   *
   * @param message
   *          the exception message
   * @param cause
   *          the exception cause
   */
  public ReflectionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new ReflectionException with the specified message.
   *
   * @param message
   *          the exception message
   */
  public ReflectionException(String message) {
    super(message);
  }

}
