/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.reflection;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.TruggerTest.assertThrow;
import static tools.devnull.trugger.reflection.Reflection.invoke;
import static tools.devnull.trugger.reflection.Reflection.reflect;

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
            .from(ClassWithNoDeclaredConstructor.class)
            .result()
    );
    assertNotNull(
        reflect().visible().constructor()
            .withoutParameters()
            .from(ClassWithNoDeclaredConstructor.class)
            .result()
    );
    assertEquals(
        1,
        reflect().constructors().from(ClassWithNoDeclaredConstructor.class).size()
    );
  }

  @Test
  public void testInvoker() {
    Constructor<?> constructor = reflect().constructor()
        .withoutParameters()
        .from(ArrayList.class)
        .result();
    assertNotNull(constructor);
    Object object = invoke(constructor).withoutArgs();
    assertTrue(object instanceof ArrayList);
  }

  @Test
  public void testExceptionHandling() {
    Constructor<?> constructor = reflect().constructor().from(TestObject.class).result();
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
