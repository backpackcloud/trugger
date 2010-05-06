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
package net.sf.trugger.test.element;

import static net.sf.trugger.element.Elements.element;
import static net.sf.trugger.element.Elements.elements;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementPredicates;
import net.sf.trugger.test.Flag;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementTest {

  @Test
  public void elementTest() {
    Element el = element("age").annotatedWith(Flag.class).in(TestObject.class);
    assertNotNull(el);
    assertEquals("age", el.name());
    assertEquals(TestObject.class, el.declaringClass());
    assertEquals(1, el.getDeclaredAnnotations().length);
    assertTrue(el.isAnnotationPresent(Flag.class));
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    assertFalse(el.isSpecific());

    el = element("age").notAnnotatedWith(Flag.class).in(TestObject.class);
    assertNull(el);

    el = element("staticValue").in(TestObject.class);
    assertEquals("staticValue", el.name());
    assertTrue(el.isSpecific());
    assertTrue(el.isReadable());
    assertTrue(el.isWritable());
    TestObject.staticValue = 0.0;

    assertEquals(0.0, el.value());
    el.value(1.5);
    assertEquals(1.5, el.value());
  }

  @Test
  public void equalsAndHashTest() {
    Element el = element("age").in(TestObject.class);
    Element el2 = element("age").in(TestObject.class);
    assertTrue(el.equals(el2));
    assertFalse(el.isSpecific());
    assertFalse(el2.isSpecific());

    el = element("age").in(TestObject.class);
    el2 = element("age").in(TestObject.class);
    assertTrue(el.equals(el2));
    assertFalse(el.isSpecific());
    assertFalse(el2.isSpecific());
    assertMatch(el, ElementPredicates.NON_SPECIFIC);

    TestObject t1 = new TestObject("name", "last name");
    TestObject t2 = new TestObject("name", "last name");

    el = element("age").in(t1);
    el2 = element("age").in(t2);
    assertFalse(el.equals(el2));
    assertTrue(el.isSpecific());
    assertTrue(el2.isSpecific());

    el = element("age").in(t1);
    el2 = element("age").in(t2);
    assertFalse(el.equals(el2));
    assertTrue(el.isSpecific());
    assertTrue(el2.isSpecific());
    assertMatch(el, ElementPredicates.SPECIFIC);

    BindableElement bel = element("age").forBind().in(t1);
    BindableElement bel2 = element("age").forBind().in(t2);
    assertFalse(bel.equals(bel2));
    assertTrue(bel.isSpecific());
    assertTrue(bel2.isSpecific());

    bel2 = element("age").forBind().in(t1);
    assertTrue(bel.equals(bel2));

    Set<Element> elements = elements().in(t1);
    assertMatch(elements, ElementPredicates.SPECIFIC);

    elements = elements().in(t1);
    assertMatch(elements, ElementPredicates.SPECIFIC);

    elements = elements().nonSpecific().in(TestObject.class);
    assertMatch(elements, ElementPredicates.NON_SPECIFIC);
  }

  @Test
  public void testNullSpecificElement() {
    Element el = element("non_existent").in(new Object());
    assertNull(el);
  }

}
