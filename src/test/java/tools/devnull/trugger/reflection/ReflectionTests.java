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
import tools.devnull.trugger.reflection.impl.TruggerFieldsSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.TruggerTest.assertThrow;
import static tools.devnull.trugger.reflection.Reflection.reflect;
import static tools.devnull.trugger.reflection.Reflection.wrapperFor;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ReflectionTests {

  @Test
  public void testWrapperResolver() {
    assertEquals(Boolean.class, wrapperFor(boolean.class));
    assertEquals(Byte.class, wrapperFor(byte.class));
    assertEquals(Short.class, wrapperFor(short.class));
    assertEquals(Character.class, wrapperFor(char.class));
    assertEquals(Integer.class, wrapperFor(int.class));
    assertEquals(Long.class, wrapperFor(long.class));
    assertEquals(Float.class, wrapperFor(float.class));
    assertEquals(Double.class, wrapperFor(double.class));
  }

  private static class TestObject {

    public TestObject(Collection c) {
    }

    public TestObject(int i) {
    }

    public TestObject() {
    }

  }

  private class GenericClass<E> {
  }

  @Test
  public void testGenericTypeResolver() throws Exception {
    final Map<String, Integer> map = new HashMap<String, Integer>() {
    };
    assertEquals(String.class, reflect().genericType("K", map));
    assertEquals(Integer.class, reflect().genericType("V", map));

    GenericClass<String> genericClass = new GenericClass<String>() {
    };
    assertEquals(String.class, reflect().genericType("E", genericClass));
    assertEquals(String.class, reflect().genericTypeOf(genericClass));

    assertThrow(ReflectionException.class, () -> {
      reflect().genericTypeOf(map);
    });

    assertThrow(ReflectionException.class, () -> {
      reflect().genericTypeOf(Object.class);
    });
  }

  public static class DeclaredTest {

    public String name;
    private String phone;

    public DeclaredTest() {

    }

    private DeclaredTest(String name) {

    }

    public void foo() {

    }

    private void bar() {

    }

  }

  @Test
  public void testDeclared() {
    assertEquals(
        1, reflect().visible().constructors().from(DeclaredTest.class).size()
    );
    assertEquals(
        1, reflect().visible().fields().from(DeclaredTest.class).size()
    );
    assertTrue(
        reflect().visible().methods().from(DeclaredTest.class).size() > 1
    );

    assertEquals(
        2, reflect().declared().constructors().from(DeclaredTest.class).size()
    );
    assertEquals(
        2, reflect().declared().fields().from(DeclaredTest.class).size()
    );
    assertEquals(
        2, reflect().declared().methods().from(DeclaredTest.class).size()
    );
  }

  @Test
  public void testInterfacesReflection() {
    List<Class> interfaces =
        reflect().interfacesOf(TruggerFieldsSelector.class);
    assertEquals(3, interfaces.size());
  }

  @Test
  public void testHierarchy() {
    Iterable<Class> iterable = Reflection.hierarchyOf(IllegalArgumentException.class);
    Iterator<Class> iterator = iterable.iterator();

    assertTrue(iterator.hasNext());
    assertEquals(IllegalArgumentException.class, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(RuntimeException.class, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(Exception.class, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(Throwable.class, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(Object.class, iterator.next());

    assertFalse(iterator.hasNext());

    iterator = Reflection.hierarchyOf(Object.class).iterator();

    assertTrue(iterator.hasNext());
    assertEquals(Object.class, iterator.next());

    assertFalse(iterator.hasNext());
  }

}
