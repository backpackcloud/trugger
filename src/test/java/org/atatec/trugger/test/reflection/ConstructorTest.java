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
package org.atatec.trugger.test.reflection;

import org.atatec.trugger.reflection.ReflectionException;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorTest {

  public static class ClassWithNoDeclaredConstructor {

  }

  public static class TestObject {

    public TestObject() throws Throwable {
      throw new IllegalArgumentException();
    }

  }

  @Test
  public void testNotDeclaredConstructor() {
    assertNotNull(
        reflect().constructor()
            .withoutParameters()
            .in(ClassWithNoDeclaredConstructor.class)
    );
    assertNotNull(
        reflect().visible().constructor()
            .withoutParameters()
            .in(ClassWithNoDeclaredConstructor.class)
    );
    assertEquals(
        1,
        reflect().constructors().in(ClassWithNoDeclaredConstructor.class).size()
    );
  }

  @Test
  public void testInvoker() {
    Constructor<?> constructor = reflect().constructor()
        .withoutParameters()
        .in(ArrayList.class);
    assertNotNull(constructor);
    Object object = invoke(constructor).withoutArgs();
    assertTrue(object instanceof ArrayList);
  }

  @Test
  public void testExceptionHandling() {
    Constructor<?> constructor = reflect().constructor().in(TestObject.class);
    assertThrow(ReflectionException.class,
        () -> invoke(constructor).withoutArgs()
    );
    try {
      invoke(constructor).withoutArgs();
    } catch (ReflectionException e) {
      assertTrue(IllegalArgumentException.class.equals(e.getCause().getClass()));
    }
  }

}
