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

import org.atatec.trugger.element.Element;
import org.junit.Before;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.List;
import java.util.function.Function;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.kodo.Scenario.should;
import static org.kodo.Spec.be;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ArrayElementTest implements BaseElementTest {

  private int[] ints;

  private TestObject[] objects;

  private int index;

  private Function<List<Element>, ?> first() {
    return (list) -> list.get(index).value();
  }

  private Function<List<Element>, ?> next() {
    return (list) -> list.get(++index).value();
  }

  @Before
  public void initialize() {
    ints = new int[]{0, 10, 12, 33};
    objects = new TestObject[]{new TestObject("name", "lastname")};
    index = 0;
  }

  @Test
  public void testFindingAll() {
    TestScenario.given(elements().in(ints))
        .the(List::size, should(be(4)))
        .the(first(), should(be(0)))
        .the(next(), should(be(10)))
        .the(next(), should(be(12)))
        .the(next(), should(be(33)));
  }

  @Test
  public void testIndexElements() {
    TestScenario.given(element("0").in(ints))
        .thenIt(should(be(readable())))
        .thenIt(should(be(writable())))
        .the(type(), should(be(int.class)))
        .the(declaringClass(), should(be(int[].class)))
        .the(value(), should(be(0)))

        .when(valueIsSetTo(15))
        .the(value(), should(be(15)));

    TestScenario.given(element("ints.1").in(this))
        .thenIt(should(be(readable())))
        .thenIt(should(be(writable())))
        .the(type(), should(be(int.class)))
        .the(declaringClass(), should(be(ArrayElementTest.class)))
        .the(value(), should(be(10)))

        .when(valueIsSetTo(15))
        .the(value(), should(be(15)));

    TestScenario.given(element("0").in(objects))
        .thenIt(should(be(readable())))
        .thenIt(should(be(writable())))
        .the(type(), should(be(TestObject.class)))
        .the(declaringClass(), should(be(TestObject[].class)));
  }

  @Test
  public void testReferencedElements() {
    TestScenario.given(element("first").in(ints))
        .the(value(), should(be(0)));

    TestScenario.given(element("last").in(ints))
        .the(value(), should(be(33)));
  }

}
