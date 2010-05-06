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
import static net.sf.trugger.test.TruggerTest.assertElements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.Set;

import net.sf.trugger.element.Element;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertiesElementTest {
  
  @Test
  public void propertiesElementTest() {
    Properties properties = new Properties();
    properties.setProperty("login", "admin");
    properties.setProperty("password", "admin");
    
    Set<Element> elements = elements().in(properties);
    assertElements(elements, "login", "password");
    
    Element element = element("login").in(properties);
    assertEquals(String.class, element.type());
    assertEquals("login", element.name());
    assertEquals("admin", element.value());
    element.value("guest");
    assertEquals("guest", element.value());
    assertEquals("guest", properties.getProperty("login"));
    
    assertTrue(element.isReadable());
    assertTrue(element.isWritable());
  }
}
