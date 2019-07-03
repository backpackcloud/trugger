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

import io.backpackcloud.trugger.Flag;
import org.junit.Test;

import java.lang.reflect.Method;

import static io.backpackcloud.trugger.reflection.MethodPredicates.annotated;
import static io.backpackcloud.trugger.reflection.MethodPredicates.annotatedWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * A class for testing method reflection by the {@link Reflector}.
 *
 * @author Marcelo Guimaraes
 */
public class MethodReflectionTest {

  interface TestInterface {

    void doIt();

    void foo();

    @Flag
    void bar();

  }

  @Test
  public void invokerTest() {
    TestInterface obj = mock(TestInterface.class);
    Method method = Reflection.reflect().method("doIt").from(obj).map(ReflectedMethod::actualMethod).get();
    Reflection.invoke(method).on(obj).withoutArgs();
    verify(obj).doIt();
  }

  @Test
  public void invokerExceptionHandlerTest() {
    TestInterface obj = mock(TestInterface.class);
    doThrow(new IllegalArgumentException()).when(obj).doIt();
    try {
      Method method = Reflection.reflect().method("doIt").from(obj).map(ReflectedMethod::actualMethod).get();
      Reflection.invoke(method).on(obj).withoutArgs();
      throw new AssertionError();
    } catch (ReflectionException e) {
      assertEquals(IllegalArgumentException.class, e.getCause().getClass());
    }

    verify(obj).doIt();
  }

  @Test
  public void invokerForNoMethodTest() {
    TestInterface obj = mock(TestInterface.class);
    Method notDeclared = Reflection.reflect()
        .method("notDeclared")
        .from(obj)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);
    Reflection.invoke(notDeclared).on(obj);
  }

  @Test
  public void predicatesTest() {
    assertFalse(
        annotated().test(
            Reflection.reflect().method("doIt").from(TestInterface.class).map(ReflectedMethod::actualMethod).get()
        )
    );
    assertFalse(
        annotatedWith(Flag.class).test(
            Reflection.reflect().method("doIt").from(TestInterface.class).map(ReflectedMethod::actualMethod).get()
        )
    );
    assertTrue(
        annotated().test(
            Reflection.reflect().method("bar").from(TestInterface.class).map(ReflectedMethod::actualMethod).get()
        )
    );
    assertTrue(
        annotatedWith(Flag.class).test(
            Reflection.reflect().method("bar").from(TestInterface.class).map(ReflectedMethod::actualMethod).get()
        )
    );
  }

}
