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
import org.atatec.trugger.test.Should;
import org.atatec.trugger.test.TestScenario;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ArrayElementTest extends BaseElementTest {

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
        .the(List::size, Should.be(4))
        .the(first(), Should.be(0))
        .the(next(), Should.be(10))
        .the(next(), Should.be(12))
        .the(next(), Should.be(33));
  }

  @Test
  public void testIndexElements() {
    TestScenario.given(element("0").in(ints))
        .thenIt(Should.be(readable()))
        .thenIt(Should.be(writable()))
        .the(type(), Should.be(int.class))
        .the(declaringClass(), Should.be(int[].class))
        .the(value(), Should.be(0))

        .when(valueIsSetTo(15))
        .the(value(), Should.be(15));

    TestScenario.given(element("ints.1").in(this))
        .thenIt(Should.be(readable()))
        .thenIt(Should.be(writable()))
        .the(type(), Should.be(int.class))
        .the(declaringClass(), Should.be(ArrayElementTest.class))
        .the(value(), Should.be(10))

        .when(valueIsSetTo(15))
        .the(value(), Should.be(15));

    TestScenario.given(element("0").in(objects))
        .thenIt(Should.be(readable()))
        .thenIt(Should.be(writable()))
        .the(type(), Should.be(TestObject.class))
        .the(declaringClass(), Should.be(TestObject[].class));
  }

  @Test
  public void testReferencedElements() {
    TestScenario.given(element("first").in(ints))
        .the(value(), Should.be(0));

    TestScenario.given(element("last").in(ints))
        .the(value(), Should.be(33));
  }

}
