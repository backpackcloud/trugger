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
package com.backpackcloud.trugger.reflection;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static com.backpackcloud.trugger.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimaraes
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
        Reflection.reflect().constructor()
            .withoutParameters()
            .from(ClassWithNoDeclaredConstructor.class)
            .orElse(null)
    );
    assertNotNull(
        Reflection.reflect().visible().constructor()
            .withoutParameters()
            .from(ClassWithNoDeclaredConstructor.class)
            .orElse(null)
    );
    assertEquals(
        1,
        Reflection.reflect().constructors().from(ClassWithNoDeclaredConstructor.class).size()
    );
  }

  @Test
  public void testInvoker() {
    Constructor<?> constructor = Reflection.reflect().constructor()
        .withoutParameters()
        .from(ArrayList.class)
        .map(ReflectedConstructor::unwrap)
        .orElse(null);
    assertNotNull(constructor);
    Object object = Reflection.invoke(constructor).withoutArgs();
    assertTrue(object instanceof ArrayList);
  }

  @Test
  public void testExceptionHandling() {
    Constructor<?> constructor = Reflection.reflect()
        .constructor()
        .from(TestObject.class)
        .map(ReflectedConstructor::unwrap)
        .orElse(null);
    assertThrow(ReflectionException.class,
        () -> Reflection.invoke(constructor).withoutArgs()
    );
    try {
      Reflection.invoke(constructor).withoutArgs();
    } catch (ReflectionException e) {
      assertTrue(IllegalArgumentException.class.equals(e.getCause().getClass()));
    }
  }

}
