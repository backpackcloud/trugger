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
import tools.devnull.kodo.TestScenario;
import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Result;
import tools.devnull.trugger.reflection.ClassPredicates;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static tools.devnull.kodo.Spec.*;
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

  private Consumer<? super Class<Elements>> testFinderIsRegistered() {
    return (elements) -> Elements.registry().register(new MyFinder())
        .to(ClassPredicates.type(TestFinder.class));
  }

  @Test
  public void testRegistry() {
    TestScenario.given(Elements.class)
        .the(element("field").in(TestFinder.class), should(notBe(NULL)))
        .the(elements().in(TestFinder.class), should(notBe(EMPTY)))
        .when(testFinderIsRegistered())
        .the(element("field").in(TestFinder.class), should(be(NULL)))
        .the(elements().in(TestFinder.class), should(be(EMPTY)));
  }

}
