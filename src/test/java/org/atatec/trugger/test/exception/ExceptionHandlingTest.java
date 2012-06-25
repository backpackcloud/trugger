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

package org.atatec.trugger.test.exception;

import org.atatec.trugger.TruggerException;
import org.atatec.trugger.exception.CompositeExceptionHandler;
import org.atatec.trugger.exception.ExceptionHandlers;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertTrue;
import static org.atatec.trugger.test.TruggerTest.assertThrow;

/** @author Marcelo Varella Barca Guimarães */
public class ExceptionHandlingTest {

  CompositeExceptionHandler handler;

  @Before
  public void initialize() {
    handler = new CompositeExceptionHandler();
  }

  @Test
  public void testNoConfiguredExceptionHandler() {
    handler.handle(new NullPointerException());
  }

  @Test(expected = NullPointerException.class)
  public void testThrowExceptionHandler() {
    handler.throwEvery(NullPointerException.class);
    handler.handle(new NullPointerException());
  }

  @Test(expected = TruggerException.class)
  public void testDefaultExceptionHandler() {
    handler.use(ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER);
    handler.handle(new Exception());
  }

  @Test(expected = NullPointerException.class)
  public void testDefaultExceptionHandlerWithRuntimeException() {
    handler.use(ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER);
    handler.handle(new NullPointerException());
  }

  @Test(expected = RuntimeException.class)
  public void testEncapsulateException() {
    handler.encapsulateEvery(Exception.class).with(RuntimeException.class);
    handler.handle(new InvocationTargetException(new NullPointerException()));
  }

  @Test
  public void testBehaviour() {
    handler.silence(NullPointerException.class);
    handler.encapsulateEveryCauseOf(InvocationTargetException.class).with(RuntimeException.class);
    handler.throwEvery(IllegalStateException.class);
    handler.throwEveryCauseOf(Error.class);
    handler.silence(TruggerException.class);
    handler.handle(Throwable.class).with(ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER);

    handler.handle(new TruggerException());
    assertThrow(new Runnable() {
      public void run() {
        handler.handle(new Error(new NullPointerException()));
      }
    }, NullPointerException.class);
    assertThrow(new Runnable() {
      public void run() {
        handler.handle(new InvocationTargetException(new NullPointerException()));
      }
    }, RuntimeException.class);
    try {
      handler.handle(new InvocationTargetException(new NullPointerException()));
    } catch (RuntimeException e) {
      assertTrue(e.getCause() instanceof NullPointerException);
    }
    assertThrow(new Runnable() {
      public void run() {
        handler.handle(new IllegalStateException());
      }
    }, IllegalStateException.class);
    handler.handle(new NullPointerException());
  }

}
