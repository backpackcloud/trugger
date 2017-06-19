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

import org.junit.Before;
import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.util.List;
import java.util.function.Function;

import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.writable;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class ArrayElementTest implements ElementExpectations {

  private int[] ints;

  private TestObject[] objects;

  private int index;

  private Function<List<Element>, ?> first() {
    return (list) -> list.get(index).getValue();
  }

  private Function<List<Element>, ?> next() {
    return (list) -> list.get(++index).getValue();
  }

  @Before
  public void initialize() {
    ints = new int[]{0, 10, 12, 33};
    objects = new TestObject[]{new TestObject("name", "lastname")};
    index = 0;
  }

  @Test
  public void testFindingAll() {
    Spec.given(elements().from(ints))
        .expect(List::size, to().be(4))
        .expect(first(), to().be(0))
        .expect(next(), to().be(10))
        .expect(next(), to().be(12))
        .expect(next(), to().be(33));
  }

  @Test
  public void testIndexElements() {
    Spec.given(element("0").from(ints).result())
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))

        .expect(Element::type, to().be(int.class))
        .expect(Element::declaringClass, to().be(int[].class))
        .expect(Element::getValue, to().be(0))

        .when(valueIsSetTo(15))
        .expect(Element::getValue, to().be(15));

    Spec.given(element("ints.1").from(this).result())
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(Element::type, to().be(int.class))
        .expect(Element::declaringClass, to().be(ArrayElementTest.class))
        .expect(Element::getValue, to().be(10))

        .when(valueIsSetTo(15))
        .expect(Element::getValue, to().be(15));

    Spec.given(element("0").from(objects).result())
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(Element::type, to().be(TestObject.class))
        .expect(Element::declaringClass, to().be(TestObject[].class));
  }

  @Test
  public void testReferencedElements() {
    Spec.given(element("first").from(ints).result())
        .expect(Element::getValue, to().be(0));

    Spec.given(element("last").from(ints).result())
        .expect(Element::getValue, to().be(33));
  }

}
