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
package net.sf.trugger.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.trugger.test.TruggerTest;
import net.sf.trugger.util.MultiResourceBundle;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MultiResourceBundleTest {
  
  private ResourceBundle defaultBundle;
  private ResourceBundle firstBundle;
  private ResourceBundle secondBundle;
  
  private ResourceBundle merged;
  
  @Before
  public void initialize() {
    defaultBundle = ResourceBundle.getBundle("net.sf.trugger.test.general.default", new Locale("pt"));
    firstBundle = ResourceBundle.getBundle("net.sf.trugger.test.general.first-level", Locale.ENGLISH);
    secondBundle = ResourceBundle.getBundle("net.sf.trugger.test.general.second-level", Locale.ITALIAN);
    
    merged = MultiResourceBundle.wrap(defaultBundle).merge(firstBundle, secondBundle);
  }
  
  @Test
  public void keysTest() {
    assertTrue(merged.containsKey("key"));
    assertTrue(merged.containsKey("test"));
    assertTrue(merged.containsKey("do"));
    assertTrue(merged.containsKey("foo"));
    assertTrue(merged.containsKey("logic"));
    assertFalse(merged.containsKey("not-exist"));
  }
  
  @Test
  public void localeTest() {
    assertEquals(Locale.ITALIAN, merged.getLocale());
  }
  
  @Test
  public void integrityTest() {
    Enumeration<String> keys = merged.getKeys();
    Set<String> keySet = new HashSet<String>();
    int i = 0;
    while (keys.hasMoreElements()) {
      i++;
      String key = keys.nextElement();
      assertNotNull(merged.getString(key));
      keySet.add(key);
    }
    assertEquals(5, i);
    assertEquals(keySet, merged.keySet());
  }
  
  @Test
  public void valuesTest() throws Exception {
    assertEquals("VALUE", merged.getString("key"));
    assertEquals("case", merged.getString("test"));
    assertEquals("not", merged.getString("do"));
    assertEquals("BAR", merged.getString("foo"));
    assertEquals("boolean", merged.getString("logic"));
    TruggerTest.assertThrow(new Runnable() {
      public void run() {
        merged.getString("not-exist");
      }
    }, MissingResourceException.class);
  }
  
}
