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

package com.backpackcloud.trugger.interception;

import org.junit.Test;
import com.backpackcloud.trugger.Flag;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static com.backpackcloud.trugger.TruggerTest.assertThrow;

public class InterceptionContextTest {

  interface Validator<T> {

    @Flag
    boolean validate(T arg);

  }

  @Test
  public void testContext() {
    Validator validator = Interception.intercept(Validator.class)
        .onCall(c -> {
          assertEquals(1, c.args().length);
          assertEquals("test", c.args()[0]);
          assertNull(c.target());
          assertEquals("validate", c.method().getName());
          assertTrue(c.method().isAnnotationPresent(Flag.class));
          assertTrue(Proxy.isProxyClass(c.proxy().getClass()));
          return true;
        }).proxy();

    assertTrue(validator.validate("test"));
  }

  @Test
  public void testContextWithTarget() {
    Validator<String> notEmpty = new Validator<String>() {
      @Override
      public boolean validate(@Flag String arg) {
        return !arg.isEmpty();
      }
    };
    Validator validator = Interception.intercept(Validator.class)
        .on(notEmpty)
        .onCall(c -> {
          assertTrue(c.method().isAnnotationPresent(Flag.class));
          Method targetMethod = c.targetMethod();
          assertFalse(targetMethod.isAnnotationPresent(Flag.class));
          assertTrue(targetMethod.getParameters()[0]
              .isAnnotationPresent(Flag.class));
          assertSame(notEmpty, c.target());
          return c.invoke();
        }).proxy();
    assertFalse(validator.validate(""));
    assertTrue(validator.validate("non empty"));
    assertThrow(NullPointerException.class, () -> validator.validate(null));
  }


}
