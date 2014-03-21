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

import org.atatec.trugger.interception.Interception;
import org.atatec.trugger.interception.Interceptor;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/** @author Marcelo Varella Barca Guimarães */
public class InterceptorTest {

  Interception action = context -> context.invokeMethod();

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
    MyInterface obj = new Interceptor(action).createProxy().over(new InvocationTest()).forAllInterfaces();
    Object argument = new Object();
    assertSame(argument, obj.doIt(argument));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInterfaceExceptionHandling() {
    MyInterface obj = new Interceptor(action).createProxy().over(new ExceptionHandlingTest()).forAllInterfaces();
    obj.doIt(null);
  }

}
