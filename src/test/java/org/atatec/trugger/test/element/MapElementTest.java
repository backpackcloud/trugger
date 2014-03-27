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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.*;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MapElementTest {
  
  @Test
  public void mapElementTest() {
    Map<String, Object> map1 = new HashMap<String, Object>();
    Map<String, Object> map2 = new HashMap<String, Object>();
    Map<String, Object> map3 = new HashMap<String, Object>();
    BundleBean obj = new BundleBean();
    
    map1.put("foo", "bar");
    map1.put("framework", "trugger");
    map1.put("author", "marcelo");
    map1.put("obj", obj);
    
    map2.put("foo", "bar2");
    map2.put("framework", "trugger2");
    map2.put("author", "marcelo2");
    map2.put("obj", obj);
    
    map3.putAll(map1);
    
    Set<Element> elements = elements().in(Map.class);
    assertTrue(elements.isEmpty());
    
    elements = elements().in(map1);
    assertFalse(elements.isEmpty());
    
    assertElements(elements, "foo", "framework", "author", "obj");
    
    testMap(map1, map2, "foo", "bar", "bar2");
    testMap(map1, map2, "framework", "trugger", "trugger2");
    testMap(map1, map2, "author", "marcelo", "marcelo2");
    
    final Element element = element("obj.bundle.framework").in(map3);
    assertTrue(element.isSpecific());
    assertEquals("trugger", element.value());
    assertThrow(HandlingException.class, () -> element.value("none"));
  }
  
  private void testMap(final Map map1, final Map map2, String key,
                       Object value1, Object value2) {
    final Element element = element(key).in(map1);
    assertNotNull(element);
    assertTrue(element.isSpecific());
    
    assertEquals(Map.class, element.declaringClass());
    
    assertEquals(value1, element.value());
    assertEquals(key, element.name());
    assertEquals(value1, element.in(map1).value());
    assertEquals(value2, element.in(map2).value());
    assertTrue(element.isReadable());
    assertTrue(element.isWritable());
    
    element.value("modified");
    element.in(map2).value("modified");

    assertEquals("modified", map1.get(key));
    assertEquals("modified", map2.get(key));
    assertEquals("modified", element.value());
    assertEquals("modified", element.in(map1).value());
    assertEquals("modified", element.in(map2).value());
  }
}
