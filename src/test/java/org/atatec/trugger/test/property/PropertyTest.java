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
import org.atatec.trugger.property.PropertyFactory;
import org.atatec.trugger.property.impl.TruggerPropertyFactory;
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

  private Element getProperty(String name) {
    return getProperty(name, TestObject.class);
  }

  private Element getProperty(String name, Object target) {
    //doing this we make sure that two properties having the same name will have diferent IDs
    PropertyFactory factory = new TruggerPropertyFactory();
    return factory.createPropertySelector(name).in(target);
  }

  @Before
  public void initialize() {
    test = new TestObject("test", 10, 15.00, false, null);
  }

  private void assertEqualsAndHash(Element property1, Element property2) {
    assertEquals(property1.hashCode(), property2.hashCode());
    assertTrue(property1.equals(property2));
    assertEquals(property1, property2);
  }

  @Test
  public void propertyTest() {
    Element property1 = getProperty("name", test);
    Element property2 = getProperty("name", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(String.class, property1.type());
    assertTrue(property1.isAnnotationPresent(Flag.class));
    assertEquals("test", property1.in(test).value());

    property1 = getProperty("price", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("price", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(double.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    Double value = property1.in(test).value();
    assertEquals(15.00, value.doubleValue(), 0.01);
    assertEqualsAndHash(property1, property2);

    property1 = getProperty("active", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("active", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertTrue(property1.isWritable());
    assertEquals(boolean.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    assertEquals(false, property1.in(test).value());
    property1.in(test).value(true);
    assertEquals(true, property1.in(test).value());
    assertEqualsAndHash(property1, property2);

    property1 = getProperty("class", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("class", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEqualsAndHash(property1, property2);

    assertNull(getProperty("age"));

    property1 = getProperty("fieldProp", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("fieldProp", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertTrue(property1.isWritable());
    assertEquals(long.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    property1.in(test).value(10L);
    assertEquals(10L, (long) property1.in(test).value());
    assertEqualsAndHash(property1, property2);

    property1 = getProperty("otherFieldProp", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("otherFieldProp", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(long.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    assertEquals(10L, (long) property1.in(test).value());
    assertEqualsAndHash(property1, property2);
  }

  @Test(expected = UnwritableElementException.class)
  public void unwritablePropertyTest() {
    getProperty("readable").in(test).value(new Object());
  }

  @Test
  public void allAccessPropertyTest() {
    Element property = getProperty("allAccess");
    assertNotNull(property);
    Object value = new Object();
    property.in(test).value(value);
    assertSame(value, property.in(test).value());
  }

  @Test
  public void copyTest() {
    TestObject o1 = new TestObject("name", 15, 12.00, false, "read1");
    TestObject o2 = new TestObject(null, 20, 13.00, true, "read2");
    o1.allAccess = "all1";
    o2.allAccess = "all1";

    ElementsSelector selection = Elements.elements()
        .filter(ElementPredicates.ofType(boolean.class).negate());

    Elements.copy(selection).from(o1).notNull().to(o2);

    assertNotNull(o1.getName()); // a null value indicates that the null property has been copied
    assertNotSame(o1.active, o2.active); // not covered by the selection
    assertSame(o1.age, o2.age);
    assertNotSame(o1.price, o2.price); //only writable
    assertFalse(o1.readable.equals(o2.readable)); // only readable
    assertTrue(o1.allAccess.equals(o2.allAccess));
  }

}
