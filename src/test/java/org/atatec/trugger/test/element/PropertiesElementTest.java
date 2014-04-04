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

import java.util.List;
import java.util.Properties;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.assertElements;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertiesElementTest {
  
  @Test
  public void propertiesElementTest() {
    Properties properties = new Properties();
    properties.setProperty("login", "admin");
    properties.setProperty("password", "admin");
    
    List<Element> elements = elements().in(properties);
    assertElements(elements, "login", "password");
    elements = elements().in(Properties.class);
    assertTrue(elements.isEmpty());
    
    Element element = element("login").in(properties);
    assertEquals(String.class, element.type());
    assertEquals("login", element.name());
    assertEquals("admin", element.get());
    element.set("guest");
    assertEquals("guest", element.get());
    assertEquals("guest", properties.getProperty("login"));
    
    assertTrue(element.isReadable());
    assertTrue(element.isWritable());
    assertThrow(HandlingException.class, element,
        (el) -> el.set(new Object()));
    assertThrow(IllegalArgumentException.class, element,
        (el) -> el.in(new Object()).set(""));

    element = element("login").in(Properties.class);
    assertEquals(String.class, element.type());
    assertEquals(Properties.class, element.declaringClass());
  }
}
