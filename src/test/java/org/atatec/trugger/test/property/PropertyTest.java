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
package org.atatec.trugger.test.property;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.element.UnwritableElementException;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.test.Flag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the properties implementations.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertyTest {

  private TestObject test;

  private Element getElement(String name) {
    return getElement(name, TestObject.class);
  }

  private Element getElement(String name, Object target) {
    return Elements.element(name).in(target);
  }

  @Before
  public void initialize() {
    test = new TestObject("test", 10, 15.00, false, null);
  }

  @Test
  public void propertyTest() {
    Element property1 = getElement("name", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(String.class, property1.type());
    assertTrue(property1.isAnnotationPresent(Flag.class));
    assertEquals("test", property1.in(test).get());

    property1 = getElement("price", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(double.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    Double value = property1.in(test).get();
    assertEquals(15.00, value.doubleValue(), 0.01);

    property1 = getElement("active", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertTrue(property1.isWritable());
    assertEquals(boolean.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    assertEquals(false, property1.in(test).get());
    property1.in(test).set(true);
    assertEquals(true, property1.in(test).get());

    property1 = getElement("class", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());

    assertNotNull(getElement("age"));

    property1 = getElement("fieldProp", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertTrue(property1.isWritable());
    assertEquals(long.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    property1.in(test).set(10L);
    assertEquals(10L, (long) property1.in(test).get());

    property1 = getElement("otherFieldProp", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertTrue(property1.isWritable());
    assertEquals(long.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    assertEquals(10L, (long) property1.in(test).get());
  }

  @Test(expected = UnwritableElementException.class)
  public void unwritablePropertyTest() {
    getElement("readable").in(test).set(new Object());
  }

  @Test
  public void allAccessPropertyTest() {
    Element property = getElement("allAccess");
    assertNotNull(property);
    Object value = new Object();
    property.in(test).set(value);
    assertSame(value, property.in(test).get());
  }

  @Test
  public void copyTest() {
    TestObject o1 = new TestObject("name", 15, 12.00, false, "read1");
    TestObject o2 = new TestObject(null, 20, 13.00, true, "read2");
    o1.allAccess = "all1";
    o2.allAccess = "all1";

    ElementsSelector selection = Elements.elements()
        .filter(ElementPredicates.type(boolean.class).negate());

    Elements.copy(selection).from(o1).notNull().to(o2);

    assertNotNull(o1.getName()); // a null value indicates that the null property has been copied
    assertNotSame(o1.active, o2.active); // not covered by the selection
    assertSame(o1.age, o2.age);
    assertNotSame(o1.price, o2.price); //only writable
    assertFalse(o1.readable.equals(o2.readable)); // only readable
    assertTrue(o1.allAccess.equals(o2.allAccess));
  }

}
