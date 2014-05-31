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
import org.atatec.trugger.element.Element;
import org.junit.Test;

import static org.atatec.trugger.element.Elements.element;
import static org.junit.Assert.assertEquals;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class HandleTest {

  public static class TestObject {

    String string;

  }

  @Test
  public void testHandleForSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    Element element = element().in(obj);

    assertEquals("test", element.value());

    element.set("other value");

    assertEquals("other value", obj.string);
  }

  @Test
  public void testHandlerForNonSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    Element element = element().in(TestObject.class);

    assertEquals("test", element.in(obj).value());

    element.in(obj).set("other value");

    assertEquals("other value", obj.string);
  }

  @Test(expected = HandlingException.class)
  public void testHandlingError() {
    TestObject obj = new TestObject();
    Element element = element().in(obj);

    element.set(10);
  }

}
