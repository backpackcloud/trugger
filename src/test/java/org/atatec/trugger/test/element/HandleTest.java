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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.atatec.trugger.element.ElementPredicates.specific;
import static org.atatec.trugger.element.ElementPredicates.type;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.element.Elements.handle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class HandleTest {

  public static class TestObject {

    String a;
    String b;
    String c;
    String d;

    static String e;
    static String f;
    static String g;
    static String h;

  }

  @Before
  public void before() {
    TestObject.e = null;
    TestObject.f = null;
    TestObject.g = null;
    TestObject.h = null;
  }

  @Test
  public void testHandleForSpecificElements() {
    List<Element> elements = elements()
        .filter(type(String.class).and(specific()))
        .in(TestObject.class);
    ValueHandler valueHandler = handle(elements);
    Collection<String> strings = valueHandler.value();
    assertEquals(4, strings.size());
    for (String string : strings) {
      assertNull(string);
    }
    valueHandler.value("value");
    valueHandler = handle(elements);
    strings = valueHandler.value();
    for (String string : strings) {
      assertEquals("value", string);
    }
  }

  @Test
  public void testHandlerForNonSpecificElements() {
    List<Element> elements = elements()
        .filter(type(String.class).and(specific().negate()))
        .in(TestObject.class);
    TestObject target = new TestObject();
    ValueHandler valueHandler = handle(elements, target);
    Collection<String> strings = valueHandler.value();
    assertEquals(4, strings.size());
    strings.forEach(Assert::assertNull);
    valueHandler.value("value2");
    valueHandler = handle(elements, target);
    strings = valueHandler.value();
    for (String string : strings) {
      assertEquals("value2", string);
    }
  }

  @Test(expected = HandlingException.class)
  public void testHandlingError() {
    List<Element> elements = elements()
        .filter(type(String.class).and(specific().negate()))
        .in(TestObject.class);
    ValueHandler valueHandler = handle(elements);
    valueHandler.value("");
  }

}
