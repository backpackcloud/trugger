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
import java.util.ResourceBundle;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.assertElements;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResourceBundleElementTest {
  
  @Test
  public void bundleElementsTest() {
    final ResourceBundle bundle1 = ResourceBundle.getBundle("org.atatec.trugger.test.element.bundle1");
    final ResourceBundle bundle2 = ResourceBundle.getBundle("org.atatec.trugger.test.element.bundle2");
    List<Element> elements = elements().in(ResourceBundle.class);
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
    assertEquals("trugger", nested.in(new BundleBean()).get());
    
    assertThrow(HandlingException.class, () -> {
      element("undefined").in(bundle1).get();
    });
    
    assertThrow(IllegalArgumentException.class, () -> {
      Element element = element("undefined").in(ResourceBundle.class);
      element.in(new Object()).get();
    });
  }
  
  private void testBundle(final ResourceBundle bundle1, final ResourceBundle bundle2, String key, String value1,
      String value2) {
    final Element element = element(key).in(bundle1);
    assertNotNull(element);
    assertTrue(element.isSpecific());
    
    assertEquals(ResourceBundle.class, element.declaringClass());
    
    assertEquals(key, element.name());
    assertEquals(value1, element.get());
    assertEquals(value1, element.in(bundle1).get());
    assertEquals(value2, element.in(bundle2).get());
    assertTrue(element.isReadable());
    assertFalse(element.isWritable());
    
    assertThrow(HandlingException.class,
        () -> element.set("Don't modify!"));
    
    assertThrow(HandlingException.class,
        () -> element.in(bundle2).set("Don't modify!"));
    
  }
}
