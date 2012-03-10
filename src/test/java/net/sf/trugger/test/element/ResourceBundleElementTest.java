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
package net.sf.trugger.test.element;

import static net.sf.trugger.element.Elements.element;
import static net.sf.trugger.element.Elements.elements;
import static net.sf.trugger.test.TruggerTest.assertElements;
import static net.sf.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;
import java.util.Set;

import net.sf.trugger.HandlingException;
import net.sf.trugger.element.Element;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResourceBundleElementTest {
  
  @Test
  public void bundleElementsTest() {
    final ResourceBundle bundle1 = ResourceBundle.getBundle("net.sf.trugger.test.element.bundle1");
    final ResourceBundle bundle2 = ResourceBundle.getBundle("net.sf.trugger.test.element.bundle2");
    Set<Element> elements = elements().in(ResourceBundle.class);
    assertTrue(elements.isEmpty());
    
    elements = elements().in(bundle1);
    assertFalse(elements.isEmpty());
    assertElements(elements, "foo", "framework", "author");
    
    testBundle(bundle1, bundle2, "foo", "bar", "bar2");
    testBundle(bundle1, bundle2, "framework", "trugger", "trugger2");
    testBundle(bundle1, bundle2, "author", "marcelo", "marcelo2");
    
    Element nested = element("bundle.framework").in(BundleBean.class);
    
    assertNotNull(nested);
    assertFalse(nested.isSpecific());
    assertEquals("trugger", nested.in(new BundleBean()).value());
    
    assertThrow(new Runnable() {
      
      public void run() {
        element("undefined").in(bundle1).value();
      }
    }, HandlingException.class);
    
    assertThrow(new Runnable() {
      
      public void run() {
        Element element = element("undefined").in(ResourceBundle.class);
        element.in(new Object()).value();
      }
    }, IllegalArgumentException.class);
  }
  
  private void testBundle(final ResourceBundle bundle1, final ResourceBundle bundle2, String key, String value1,
      String value2) {
    final Element element = element(key).in(bundle1);
    assertNotNull(element);
    assertTrue(element.isSpecific());
    
    assertEquals(ResourceBundle.class, element.declaringClass());
    
    assertEquals(key, element.name());
    assertEquals(value1, element.value());
    assertEquals(value1, element.in(bundle1).value());
    assertEquals(value2, element.in(bundle2).value());
    assertTrue(element.isReadable());
    assertFalse(element.isWritable());
    
    assertEquals(element, element(key).in(bundle1));
    
    assertThrow(new Runnable() {
      
      public void run() {
        element.value("Dont modify!");
      }
    }, HandlingException.class);
    
    assertThrow(new Runnable() {
      
      public void run() {
        element.in(bundle2).value("Dont modify!");
      }
    }, HandlingException.class);
    
  }
}
