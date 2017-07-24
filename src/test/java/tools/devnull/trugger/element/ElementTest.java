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
package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.trugger.Flag;

import java.io.Serializable;

import static org.junit.Assert.*;
import static tools.devnull.trugger.TruggerTest.assertThrow;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class ElementTest {

  @Test
  public void elementTest() {
    Element el = element("age")
        .filter(annotatedWith(Flag.class))
        .from(TestObject.class)
        .result();
    assertNotNull(el);
    assertEquals(
        1,
        el.getAnnotations().length
    );
    assertEquals(
        Flag.class,
        el.getAnnotation(Flag.class).annotationType()
    );
    assertEquals("age", el.name());
    assertEquals(TestObject.class, el.declaringClass());
    assertEquals(1, el.getDeclaredAnnotations().length);
    assertTrue(el.isAnnotationPresent(Flag.class));
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    assertFalse(el.isSpecific());
    assertNull(el.target());

    assertThrow(NonSpecificElementException.class,
        () -> el.getValue());
    assertThrow(NonSpecificElementException.class,
        () -> el.setValue(14));
  }

  @Test
  public void specificElementTest() {
    Element el = element("age")
        .filter(annotatedWith(Flag.class))
        .from(new TestObject("", ""))
        .result();
    assertNotNull(el);
    assertEquals(
        1,
        el.getAnnotations().length
    );
    assertEquals(
        Flag.class,
        el.getAnnotation(Flag.class).annotationType()
    );
    assertEquals("age", el.name());
    assertEquals(TestObject.class, el.declaringClass());
    assertEquals(1, el.getDeclaredAnnotations().length);
    assertTrue(el.isAnnotationPresent(Flag.class));
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    assertTrue(el.isSpecific());
    assertNotNull(el.target());

    el.setValue(14);
    assertEquals(14, (int) el.getValue());
  }

  @Test
  public void staticElementTest() {
    Element el = element("staticValue").from(new TestObject("", "")).result();
    assertEquals("staticValue", el.name());
    assertTrue(el.isSpecific());
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    TestObject.staticValue = 0.0;

    assertEquals(0.0, el.getValue(), 0e-4);
    el.setValue(1.5);
    assertEquals(1.5, el.getValue(), 0e-4);
  }

  @Test
  public void testNullSpecificElement() {
    Element el = element("non_existent").from(new Object()).result();
    assertNull(el);
  }

  @Test
  public void testSingleSelection() {
    assertNotNull(
        element().filter(ofType(int.class)).from(TestObject.class).result()
    );
    assertNull(
        element().filter(ofType(Serializable.class)).from(TestObject.class).result()
    );
    // should return the first element found, no matter what it is
    assertNotNull(
        element().filter(assignableTo(Serializable.class)).from(TestObject.class).result()
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
    Element element = element("i").from(ForMergedTest.class).result();
    assertFalse(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(5, (int) element.on(o).getValue());
    element.on(o).setValue(5);
    assertEquals(10, (int) element.on(o).getValue());

    element = element("i").from(o).result();
    assertTrue(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(10, (int) element.getValue());
    element.setValue(10);
    assertEquals(15, (int) element.getValue());

    //------------------------------------------//

    element = element("j").from(ForMergedTest.class).result();
    assertFalse(element.isSpecific());
    assertFalse(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(20, (int) element.on(o).getValue());

    element = element("j").from(o).result();
    assertTrue(element.isSpecific());
    assertFalse(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(20, (int) element.getValue());

    //------------------------------------------//

    element = element("k").from(ForMergedTest.class).result();
    assertFalse(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(0, (int) element.on(o).getValue());
    element.on(o).setValue(15);
    assertEquals(30, (int) element.on(o).getValue());

    element = element("k").from(o).result();
    assertTrue(element.isSpecific());
    assertTrue(element.isWritable());
    assertTrue(element.isReadable());
    assertEquals(30, (int) element.getValue());
    element.setValue(40);
    assertEquals(55, (int) element.getValue());
  }

}
