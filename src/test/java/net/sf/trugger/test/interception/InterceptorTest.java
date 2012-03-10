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
package net.sf.trugger.test.interception;

import static org.junit.Assert.assertSame;
import net.sf.trugger.interception.Interceptor;

import org.junit.Test;

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
    MyInterface obj = new Interceptor().createProxy().forAllInterfaces().withTarget(new InvocationTest());
    Object argument = new Object();
    assertSame(argument, obj.doIt(argument));

    obj = new Interceptor().createProxy().extending(InvocationTest.class);
    assertSame(argument, obj.doIt(argument));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInterfaceExceptionHandling() {
    MyInterface obj = new Interceptor().createProxy().forAllInterfaces().withTarget(new ExceptionHandlingTest());
    obj.doIt(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSubclassExceptionHandling() {
    MyInterface obj = new Interceptor().createProxy().extending(ExceptionHandlingTest.class);
    obj.doIt(null);
  }

}
