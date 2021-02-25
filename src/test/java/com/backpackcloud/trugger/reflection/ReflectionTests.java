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

import com.backpackcloud.trugger.reflection.impl.TruggerFieldsSelector;
import com.backpackcloud.trugger.util.ClassIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.backpackcloud.trugger.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Marcelo Guimaraes
 */
@RunWith(MockitoJUnitRunner.class)
public class ReflectionTests {

  @Test
  public void testWrapperResolver() {
    assertEquals(Boolean.class, Reflection.wrapperFor(boolean.class));
    assertEquals(Byte.class, Reflection.wrapperFor(byte.class));
    assertEquals(Short.class, Reflection.wrapperFor(short.class));
    assertEquals(Character.class, Reflection.wrapperFor(char.class));
    assertEquals(Integer.class, Reflection.wrapperFor(int.class));
    assertEquals(Long.class, Reflection.wrapperFor(long.class));
    assertEquals(Float.class, Reflection.wrapperFor(float.class));
    assertEquals(Double.class, Reflection.wrapperFor(double.class));
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
    assertEquals(String.class, Reflection.reflect().genericType("K").of(map));
    assertEquals(Integer.class, Reflection.reflect().genericType("V").of(map));

    GenericClass<String> genericClass = new GenericClass<String>() {
    };
    assertEquals(String.class, Reflection.reflect().genericType("E").of(genericClass));
    assertEquals(String.class, Reflection.reflect().genericTypeOf(genericClass));

    assertThrow(ReflectionException.class, () -> {
      Reflection.reflect().genericTypeOf(map);
    });

    assertThrow(ReflectionException.class, () -> {
      Reflection.reflect().genericTypeOf(Object.class);
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
        1, Reflection.reflect().visible().constructors().from(DeclaredTest.class).size()
    );
    assertEquals(
        1, Reflection.reflect().visible().fields().from(DeclaredTest.class).size()
    );
    assertTrue(
        Reflection.reflect().visible().methods().from(DeclaredTest.class).size() > 1
    );

    assertEquals(
        2, Reflection.reflect().declared().constructors().from(DeclaredTest.class).size()
    );
    assertEquals(
        2, Reflection.reflect().declared().fields().from(DeclaredTest.class).size()
    );
    assertEquals(
        2, Reflection.reflect().declared().methods().from(DeclaredTest.class).size()
    );
  }

  @Test
  public void testInterfacesReflection() {
    List<Class> interfaces = Reflection.reflect().interfacesOf(TruggerFieldsSelector.class);
    assertEquals(1, interfaces.size());
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

  @Test(expected = NoSuchElementException.class)
  public void testClassIteratorNext() {
    ClassIterator iterator = new ClassIterator(Object.class);
    iterator.next();
    iterator.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testClassIteratorRemove() {
    new ClassIterator(Object.class).remove();
  }

  @Mock
  ReflectedField field;

  @Mock
  ReflectedMethod method;

  @Test
  public void testFieldSetConsumer() {
    Reflection.setValue("value").accept(field);

    verify(field, times(1)).setValue("value");
  }

  @Test
  public void testMethodInvokeConsumer() {
    Object[] args = new Object[]{1, 2, 3, 4};
    Reflection.invoke(args).accept(method);

    verify(method, times(1)).invoke(args);
  }

}
