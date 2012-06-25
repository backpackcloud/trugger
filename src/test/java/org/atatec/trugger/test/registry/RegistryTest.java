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
package org.atatec.trugger.test.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.atatec.trugger.registry.MapRegistry;
import org.atatec.trugger.registry.Registry;
import org.atatec.trugger.registry.Registry.Entry;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class RegistryTest {

  private Map<String, String> map = new HashMap<String, String>();
  private Registry<String, String> register = new MapRegistry<String, String>(map);

  @Before
  public void initialize() {
    map.put("test", "value");
  }

  @Test
  public void testRegistryFor() throws Exception {
    assertEquals("value", register.registryFor("test"));
    map.clear();
    assertNull(register.registryFor("test"));
  }

  @Test
  public void testEntries() throws Exception {
    Set<Entry<String, String>> entries = register.entries();
    assertEquals(1, entries.size());
    Entry entry = entries.iterator().next();
    assertEquals("test", entry.key());
    assertEquals("value", entry.registry());
    map.clear();
    assertTrue(register.entries().isEmpty());
  }

  @Test
  public void testRemove() throws Exception {
    register.removeRegistryFor("test");
    assertNull(map.get("test"));
    assertTrue(register.entries().isEmpty());
  }

  @Test
  public void testRegistryCheck() throws Exception {
    assertTrue(register.hasRegistryFor("test"));
    map.clear();
    assertFalse(register.hasRegistryFor("test"));
  }

}
