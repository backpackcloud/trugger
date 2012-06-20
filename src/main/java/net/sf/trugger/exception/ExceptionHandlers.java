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

import net.sf.trugger.TruggerException;

/**
 * A class that holds a set of exception handlers
 *
 * @author Marcelo Varella Barca Guimarães
 * @see 3.2
 */
public class ExceptionHandlers {
  public static final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = new ExceptionHandler<Throwable>() {
    @Override
    public void handle(Throwable throwable) {
      if (throwable instanceof RuntimeException) {
        throw (RuntimeException) throwable;
      } else {
        throw new TruggerException(throwable);
      }
    }
  };
}
