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

package net.sf.trugger.exception;

/**
 * An exception handler that throws causes of exceptions.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 3.2
 */
public class CauseThrowExceptionHandler implements ExceptionHandler {

  @Override
  public void handle(Throwable throwable) {
    Throwable cause = throwable.getCause();
    if (cause instanceof RuntimeException) {
      throw (RuntimeException) cause;
    }
  }

}
