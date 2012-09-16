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
package org.atatec.trugger.test.bind;

import org.atatec.trugger.bind.Binder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.atatec.trugger.bind.Bind.binds;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/** @author Marcelo Varella Barca Guimarães */
public class ElementBindingTest {

  private static class ElementBind {

    private static boolean BOOLEAN_FIELD;

    private int intField;
    private String stringField;

    public void setIntField(int intField) {
      this.intField = intField + 5;
    }

    public void setStringField(String stringField) {
      this.stringField = stringField.toUpperCase();
    }

  }

  private Binder binder;

  @Before
  public void initialize() {
    binder = binds();
    binder.bind(10).to(element().ofType(int.class));
    binder.bind("stringField").to(elements().ofType(String.class));
    binder.bind(true).to(element("BOOLEAN_FIELD"));
  }

  @Test
  public void testGeneralElementBinding() {
    ElementBind object = new ElementBind();

    binder.applyIn(object);

    assertEquals(15, object.intField);
    assertEquals("STRINGFIELD", object.stringField);
    assertEquals(true, ElementBind.BOOLEAN_FIELD);

    object = new ElementBind();
    binds()
      .use(new ResultResolver(10)).in(element().ofType(int.class))
      .use(new ResultResolver("stringField")).in(elements().ofType(String.class))
      .use(new ResultResolver(true)).in(element("BOOLEAN_FIELD"))

      .applyIn(object);

    assertEquals(15, object.intField);
    assertEquals("STRINGFIELD", object.stringField);
    assertEquals(true, ElementBind.BOOLEAN_FIELD);
  }

  @Test
  public void testMapBinding() {
    Object value = new Object();
    binder.bind("value").to(element("key"));
    binder.bind(value).to(element("object"));

    Map<String, Object> map = new HashMap<String, Object>();

    binder.applyIn(map);

    assertFalse(map.containsKey("intField"));
    assertFalse(map.containsKey("stringField"));
    assertTrue(map.containsKey("BOOLEAN_FIELD"));
    assertTrue(map.containsKey("key"));
    assertTrue(map.containsKey("object"));

    assertEquals("value", map.get("key"));
    assertSame(value, map.get("object"));
  }

  @Test
  public void testPropertiesBind() throws Exception {
    binder.bind("value").to(element("key"));

    Properties props = new Properties();

    binder.applyIn(props);

    assertFalse(props.containsKey("intField"));
    assertFalse(props.containsKey("stringField"));
    assertFalse(props.containsKey("BOOLEAN_FIELD"));
    assertTrue(props.containsKey("key"));

    assertEquals("value", props.getProperty("key"));
  }

}
