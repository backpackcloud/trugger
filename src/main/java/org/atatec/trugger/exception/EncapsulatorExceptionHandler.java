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

package org.atatec.trugger.exception;

import org.atatec.trugger.reflection.Reflection;

/**
 * An exception handler that encapsulates exceptions.
 *
 * @author Marcelo Guimarães
 * @since 4.0
 */
public class EncapsulatorExceptionHandler implements ExceptionHandler {

  private final Class<? extends RuntimeException> encapsulate;

  public EncapsulatorExceptionHandler(Class<? extends RuntimeException> encapsulate) {
    this.encapsulate = encapsulate;
  }

  @Override
  public void handle(Throwable throwable) {
    throw Reflection.newInstanceOf(encapsulate, throwable);
  }

}
