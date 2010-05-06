/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.property;

import static net.sf.trugger.test.TruggerTest.assertElements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementPredicates;
import net.sf.trugger.element.Elements;
import net.sf.trugger.element.UnreadableElementException;
import net.sf.trugger.element.UnwritableElementException;
import net.sf.trugger.loader.ImplementationLoader;
import net.sf.trugger.property.Properties;
import net.sf.trugger.property.PropertyFactory;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.test.Flag;

import org.junit.Before;
import org.junit.Test;

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
    PropertyFactory factory = ImplementationLoader.getInstance().get(PropertyFactory.class);
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
    assertEquals(10L, property1.in(test).value());
    assertEqualsAndHash(property1, property2);
    
    property1 = getProperty("otherFieldProp", test);
    assertFalse(property1.equals(property2));
    property2 = getProperty("otherFieldProp", test);
    assertNotNull(property1);
    assertTrue(property1.isReadable());
    assertFalse(property1.isWritable());
    assertEquals(long.class, property1.type());
    assertFalse(property1.isAnnotationPresent(Flag.class));
    assertEquals(10L, property1.in(test).value());
    assertEqualsAndHash(property1, property2);
  }
  
  @Test(expected = UnreadableElementException.class)
  public void unreadablePropertyTest() {
    getProperty("writable").in(test).value();
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
    o1.writable = "write1";
    o2.writable = "write2";
    o1.allAccess = "all1";
    o2.allAccess = "all1";
    
    ElementsSelector selection = Elements.elements().thatMatches(ElementPredicates.ofType(boolean.class).negate());
    
    Elements.copyTo(o2).notNull().inSelection(selection).from(o1);
    
    assertNotNull(o1.getName()); // a null value indicates that the null property has been copied
    assertNotSame(o1.active, o2.active); // not covered by the selection
    assertSame(o1.age, o2.age);
    assertNotSame(o1.price, o2.price); //only writable
    assertFalse(o1.readable.equals(o2.readable)); // only readable
    assertFalse(o1.writable.equals(o2.writable)); // only writable
    assertTrue(o1.allAccess.equals(o2.allAccess));
  }
  
  @Test
  public void selectorTest() {
    Set<Element> props = Properties.properties().readable().in(TestObject.class);
    assertElements(props, "name", "active", "price", "allAccess", "readable", "fieldProp", "otherFieldProp", "class");
    assertNotNull(Properties.property("active").readable().in(TestObject.class));
    
    ElementsSelector selector = Properties.properties().readable().nonWritable();
    props = selector.in(TestObject.class);
    assertElements(props, "name", "price", "readable", "otherFieldProp", "class");
    assertNotNull(Properties.property("name").thatMatches(selector.toPredicate()).nonWritable().in(TestObject.class));
    assertNotNull(Properties.property("name").thatMatches(selector.toPredicate()).annotatedWith(Flag.class).in(
        TestObject.class));
    assertNull(Properties.property("name").thatMatches(selector.toPredicate()).notAnnotatedWith(Flag.class).in(
        TestObject.class));
    
    props = Properties.properties().writable().in(TestObject.class);
    assertElements(props, "active", "allAccess", "writable", "fieldProp");
    assertNotNull(Properties.property("writable").thatMatches(
        Properties.property("writable").nonReadable().toPredicate()).in(TestObject.class));
    
    props = Properties.properties().writable().thatMatches(ElementPredicates.NON_READABLE).in(TestObject.class);
    assertElements(props, "writable");
    props = Properties.properties().writable().nonReadable().in(TestObject.class);
    assertElements(props, "writable");
  }
  
}
