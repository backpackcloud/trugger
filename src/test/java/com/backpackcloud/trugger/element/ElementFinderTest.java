/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.backpackcloud.trugger.element;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.backpackcloud.trugger.element.Elements.element;
import static com.backpackcloud.trugger.element.Elements.elements;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ElementFinderTest {

  public static class MyFinder implements ElementFinder {

    @Override
    public boolean canFind(Class type) {
      return TestFinder.class.equals(type);
    }

    @Override
    public Optional<Element> find(String name, Object target) {
      return Optional.empty();
    }

    @Override
    public List<Element> findAll(Object target) {
      return Collections.emptyList();
    }

  }

  class TestFinder {
    private String field;
  }

  @Test
  public void testRegistry() {
    assertNotNull(element("field").from(TestFinder.class).orElse(null));
    assertFalse(elements().from(TestFinder.class).isEmpty());

    Elements.register(new MyFinder());

    assertNull(element("field").from(TestFinder.class).orElse(null));
    assertTrue(elements().from(TestFinder.class).isEmpty());
  }

}
