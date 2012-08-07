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

package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

/** @author Marcelo Varella Barca Guimarães */
public class ArrayElementTest {

  private int[] ints;

  private TestObject[] objects;

  private Map<String, TestObject> map;

  @Before
  public void initialize() {
    ints = new int[]{0, 10, 12, 33};
    objects = new TestObject[]{new TestObject("name", "lastname")};
    map = new HashMap<String, TestObject>();
    map.put("object", objects[0]);
  }

  @Test
  public void testIndexElements() {
    Element element = Elements.element("0").in(ints);
    assertEquals(int.class, element.type());
    assertEquals(int[].class, element.declaringClass());
    assertEquals(0, element.value());

    element = Elements.element("2").in(ints);
    assertEquals(int.class, element.type());
    assertEquals(int[].class, element.declaringClass());
    assertEquals(12, element.value());

    element = Elements.element("ints.1").in(this);
    assertEquals(int.class, element.type());
    assertEquals(ArrayElementTest.class, element.declaringClass());
    assertEquals(10, element.value());

    element = Elements.element("0").in(objects);
    assertEquals(TestObject.class, element.type());
    assertEquals(TestObject[].class, element.declaringClass());
    TestObject o = element.value();
    assertEquals("name", o.getName());
    assertEquals("lastname", o.getLastName());

    element = Elements.element("object").in(map);
    o = element.value();
    assertEquals("name", o.getName());
    assertEquals("lastname", o.getLastName());

    element = Elements.element("map.object").in(this);
    o = element.value();
    assertEquals("name", o.getName());
    assertEquals("lastname", o.getLastName());
  }

  @Test
  public void testReferencedElements() {
    Element element = Elements.element("first").in(ints);
    assertEquals(0, element.value());

    element = Elements.element("last").in(ints);
    assertEquals(33, element.value());

    element = Elements.element("first").in(objects);
    TestObject a = element.value();

    element = Elements.element("last").in(objects);
    TestObject b = element.value();

    assertSame(a, b);
  }

}
