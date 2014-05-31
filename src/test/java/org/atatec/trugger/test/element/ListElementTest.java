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

package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ListElementTest {

  private List<Integer> ints;

  private List<TestObject> objects;

  @Before
  public void initialize() {
    ints = new ArrayList();
    ints.add(0);
    ints.add(10);
    ints.add(12);
    ints.add(33);
    objects = Arrays.asList(new TestObject("name", "lastname"));
  }

  @Test
  public void testFindingAll() {
    List<Element> elements = Elements.elements().in(ints);
    assertEquals(4, elements.size());
    assertEquals(0, (int) elements.get(0).value());
    assertEquals(10, (int) elements.get(1).value());
    assertEquals(12, (int) elements.get(2).value());
    assertEquals(33, (int) elements.get(3).value());
  }

  @Test
  public void testIndexElements() {
    Element element = Elements.element("0").in(ints);

    assertTrue(element.isReadable());
    assertTrue(element.isWritable());

    assertEquals(Object.class, element.type());
    assertEquals(List.class, element.declaringClass());
    assertEquals(0, (int) element.value());

    element.set(15);

    assertEquals(15, (int) element.value());

    element = Elements.element("2").in(ints);
    assertEquals(Object.class, element.type());
    assertEquals(List.class, element.declaringClass());
    assertEquals(12, (int) element.value());

    element = Elements.element("ints.1").in(this);
    assertEquals(Object.class, element.type());
    assertEquals(ListElementTest.class, element.declaringClass());
    assertEquals(10, (int) element.value());

    element = Elements.element("0").in(objects);
    assertEquals(Object.class, element.type());
    assertEquals(List.class, element.declaringClass());
    TestObject o = element.value();
    assertEquals("name", o.getName());
    assertEquals("lastname", o.getLastName());
  }

  @Test
  public void testReferencedElements() {
    Element element = Elements.element("first").in(ints);
    assertEquals(0, (int) element.value());

    element = Elements.element("last").in(ints);
    assertEquals(33, (int) element.value());

    element = Elements.element("first").in(objects);
    TestObject a = element.value();

    element = Elements.element("last").in(objects);
    TestObject b = element.value();

    assertSame(a, b);
  }

}
