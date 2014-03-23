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
package org.atatec.trugger.test.interception;

import org.atatec.trugger.TruggerException;
import org.atatec.trugger.interception.Interceptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class InterceptorTest {

  static interface MyInterface {

    Object doIt(Object argument);

  }

  static class InvocationTest implements MyInterface {

    @Override
    public Object doIt(Object argument) {
      return argument;
    }

  }

  static class ExceptionHandlingTest implements MyInterface {

    @Override
    public Object doIt(Object argument) {
      throw new IllegalArgumentException();
    }

  }

  @Test
  public void testInterception() {
    MyInterface obj = Interceptor.intercept(new InvocationTest())
        .onCall(context -> context.invokeMethod())
        .proxy();
    Object argument = new Object();
    assertSame(argument, obj.doIt(argument));
  }

  @Test
  public void testInterfaceExceptionHandling() {
    MyInterface obj = Interceptor.intercept(new ExceptionHandlingTest())
        .onCall(context -> context.invokeMethod())
        .onError((context, error) -> "pass")
        .proxy();

    assertEquals("pass", obj.doIt(null));
  }

  @Test(expected = TruggerException.class)
  public void testInterfaceInterception() {
    MyInterface obj = Interceptor.intercept(MyInterface.class)
        .onCall(context -> {throw new IllegalArgumentException();})
        .onError((context, error) -> {throw new TruggerException(error);})
        .proxy();
    obj.doIt(null);
  }

}
