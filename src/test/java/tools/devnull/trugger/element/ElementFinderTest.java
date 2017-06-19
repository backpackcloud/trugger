/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.trugger.Optional;
import tools.devnull.trugger.reflection.ClassPredicates;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

public class ElementFinderTest {

  public static class MyFinder implements ElementFinder {

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
    assertNotNull(element("field").from(TestFinder.class).value());
    assertFalse(elements().from(TestFinder.class).isEmpty());

    Elements.registry().register(new MyFinder())
        .to(ClassPredicates.type(TestFinder.class));

    assertNull(element("field").from(TestFinder.class).value());
    assertTrue(elements().from(TestFinder.class).isEmpty());
  }

}
