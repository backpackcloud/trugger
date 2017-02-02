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

package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Result;
import tools.devnull.trugger.reflection.ClassPredicates;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 *
 */
public class ElementFinderTest {

  public static class MyFinder implements Finder<Element> {

    @Override
    public Result<Element, Object> find(String name) {
      return o -> null;
    }

    @Override
    public Result<List<Element>, Object> findAll() {
      return o -> Collections.emptyList();
    }

  }

  class TestFinder {
    private String field;
  }

  @Test
  public void testRegistry() {
    assertNotNull(element("field").in(TestFinder.class));
    assertFalse(elements().in(TestFinder.class).isEmpty());

    Elements.registry().register(new MyFinder())
        .to(ClassPredicates.type(TestFinder.class));

    assertNull(element("field").in(TestFinder.class));
    assertTrue(elements().in(TestFinder.class).isEmpty());
  }

}
