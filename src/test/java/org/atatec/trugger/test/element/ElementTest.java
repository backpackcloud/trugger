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
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import java.io.Serializable;

import static org.atatec.trugger.element.ElementPredicates.annotatedWith;
import static org.atatec.trugger.element.ElementPredicates.assignableTo;
import static org.atatec.trugger.element.ElementPredicates.type;
import static org.atatec.trugger.element.Elements.element;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementTest {

  @Test
  public void elementTest() {
    Element el = element("age")
        .filter(annotatedWith(Flag.class))
        .in(TestObject.class);
    assertNotNull(el);
    assertEquals("age", el.name());
    assertEquals(TestObject.class, el.declaringClass());
    assertEquals(1, el.getDeclaredAnnotations().length);
    assertTrue(el.isAnnotationPresent(Flag.class));
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    assertFalse(el.isSpecific());

    el = element("age")
        .filter(annotatedWith(Flag.class).negate())
        .in(TestObject.class);
    assertNull(el);

    el = element("staticValue").in(TestObject.class);
    assertEquals("staticValue", el.name());
    assertTrue(el.isSpecific());
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    TestObject.staticValue = 0.0;

    assertEquals(0.0, el.get(), 0e-4);
    el.set(1.5);
    assertEquals(1.5, el.get(), 0e-4);
  }

  @Test
  public void testNullSpecificElement() {
    Element el = element("non_existent").in(new Object());
    assertNull(el);
  }

  @Test
  public void testSingleSelection() {
    assertNotNull(
        element().filter(type(int.class)).in(TestObject.class)
    );
    assertNull(
        element().filter(type(Serializable.class)).in(TestObject.class)
    );
    // should return the first element found, no matter what is
    assertNotNull(
        element().filter(assignableTo(Serializable.class)).in(TestObject.class)
    );
  }

  private static class ForMergedTest {

    private int i;
    private final int j = 10;
    private int k;

    public int getI() {
      return i + 5;
    }

    public int getJ() {
      return j + 10;
    }

    public void setK(int k) {
      this.k = k + 15;
    }

  }

  @Test
  public void testMergedElement() throws Exception {
    ForMergedTest o = new ForMergedTest();
    Element element = element("i").in(ForMergedTest.class);
    assertFalse(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(5, (int) element.in(o).get());
    element.in(o).set(5);
    assertEquals(10, (int) element.in(o).get());

    element = element("i").in(o);
    assertTrue(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(10, (int) element.get());
    element.set(10);
    assertEquals(15, (int) element.get());

    //------------------------------------------//

    element = element("j").in(ForMergedTest.class);
    assertFalse(element.isSpecific());
    assertFalse(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(20, (int) element.in(o).get());

    element = element("j").in(o);
    assertTrue(element.isSpecific());
    assertFalse(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(20, (int) element.get());

    //------------------------------------------//

    element = element("k").in(ForMergedTest.class);
    assertFalse(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(0, (int) element.in(o).get());
    element.in(o).set(15);
    assertEquals(30, (int) element.in(o).get());

    element = element("k").in(o);
    assertTrue(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(30, (int) element.get());
    element.set(40);
    assertEquals(55, (int) element.get());
  }

}
