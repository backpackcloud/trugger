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
package net.sf.trugger.test.reflection;

import static net.sf.trugger.reflection.Reflection.newInstanceOf;
import static net.sf.trugger.reflection.Reflection.reflect;
import static net.sf.trugger.reflection.Reflection.wrapperFor;
import static net.sf.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.trugger.reflection.ReflectionException;

import org.junit.Test;

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

    public TestObject(Collection c) {}
    public TestObject(int i) {}
    public TestObject() {}

  }

  @Test
  public void testNewInstanceOf() throws Exception {
    String string = newInstanceOf(String.class, "test");
    assertEquals("test", string);
    assertNotSame("test", string);
    assertThrow(new Runnable(){
      public void run() {
        newInstanceOf(String.class, null, 1, 1);
      }
    }, ReflectionException.class);
    assertNotNull(newInstanceOf(TestObject.class));
    assertNotNull(newInstanceOf(TestObject.class, Collections.EMPTY_LIST));
    assertNotNull(newInstanceOf(TestObject.class, 15));
    assertThrow(new Runnable(){
      public void run() {
        newInstanceOf(String.class, 1, 2, 3);
      }
    }, ReflectionException.class);
  }

  private class GenericClass<E> {}

  @Test
  public void testGenericTypeResolver() throws Exception {
    final Map<String, Integer> map = new HashMap<String, Integer>(){};
    assertEquals(String.class, reflect().genericType("K").in(map));
    assertEquals(Integer.class, reflect().genericType("V").in(map));

    GenericClass<String> genericClass = new GenericClass<String>(){};
    assertEquals(String.class, reflect().genericType("E").in(genericClass));
    assertEquals(String.class, reflect().genericType().in(genericClass));

    assertThrow(new Runnable(){
      public void run() {
        reflect().genericType().in(map);
      }
    }, ReflectionException.class);

    assertThrow(new Runnable(){
      public void run() {
        reflect().genericType().in(Object.class);
      }
    }, ReflectionException.class);
  }

}
