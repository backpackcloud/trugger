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
package net.sf.trugger.test.bind;

import static net.sf.trugger.bind.Bind.newBinder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.trugger.Resolver;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
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
    binder = newBinder();
    binder.bind(10).toElements().ofType(int.class);
    binder.use(new Resolver<Object, Element>() {
      public Object resolve(Element target) {
        return target.name();
      }
    }).toElements().ofType(String.class);
    binder.bind(true).toElement("BOOLEAN_FIELD");
  }

  @Test
  public void testGeneralElementBinding() {
    ElementBind object = new ElementBind();

    binder.applyBinds(object);

    assertEquals(15, object.intField);
    assertEquals("STRINGFIELD", object.stringField);
    assertEquals(true, ElementBind.BOOLEAN_FIELD);
  }

  @Test
  public void testMapBinding() {
    Object value = new Object();
    binder.bind("value").toElement("key");
    binder.bind(value).toElement("object");

    Map<String, Object> map = new HashMap<String, Object>();

    binder.applyBinds(map);

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
    binder.bind("value").toElement("key");

    Properties props = new Properties();

    binder.applyBinds(props);

    assertFalse(props.containsKey("intField"));
    assertFalse(props.containsKey("stringField"));
    assertFalse(props.containsKey("BOOLEAN_FIELD"));
    assertTrue(props.containsKey("key"));

    assertEquals("value", props.getProperty("key"));
  }

}
