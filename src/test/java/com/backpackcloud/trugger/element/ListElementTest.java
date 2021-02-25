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

import org.junit.Before;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Arrays;
import java.util.List;

import static io.backpackcloud.kodo.Expectation.*;
import static com.backpackcloud.trugger.TruggerTest.SIZE;
import static com.backpackcloud.trugger.element.ElementPredicates.readable;
import static com.backpackcloud.trugger.element.ElementPredicates.writable;
import static com.backpackcloud.trugger.element.Elements.element;
import static com.backpackcloud.trugger.element.Elements.elements;

/**
 * @author Marcelo Guimaraes
 */
public class ListElementTest implements ElementExpectations {

  private List<Integer> ints;

  private List<TestObject> objects;

  @Before
  public void initialize() {
    ints = Arrays.asList(0, 10, 12, 33);
    objects = Arrays.asList(new TestObject("name", "lastname"));
  }

  @Test
  public void testFindingAll() {
    Spec.given(elements().from(ints))
        .expect(SIZE, to().be(4))
        .expect(elementAt(0), to().be(0))
        .expect(elementAt(1), to().be(10))
        .expect(elementAt(2), to().be(12))
        .expect(elementAt(3), to().be(33));
  }

  @Test
  public void testIndexElements() {
    Spec.given(element("0").from(ints).orElse(null))
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(List.class))
        .expect(Element::getValue, to().be(0))

        .when(valueIsSetTo(15))
        .expect(Element::getValue, to().be(15));

    Spec.given(element("2").from(ints).orElse(null))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(List.class))
        .expect(Element::getValue, to().be(12));

    Spec.given(element("ints.1").from(this).orElse(null))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(ListElementTest.class))
        .expect(Element::getValue, to().be(10));

    Spec.given(element("0").from(objects).orElse(null))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(List.class))
        .expect(valueOf("name"), to().be("name"))
        .expect(valueOf("lastName"), to().be("lastname"));
  }

  @Test
  public void testReferencedElements() {
    Spec.given(element("first").from(ints).get())
        .expect(Element::getValue, to().be(0));

    Spec.given(element("last").from(ints).get())
        .expect(Element::getValue, to().be(33));

    Object lastElementValue = element("last").from(objects).get().getValue();
    Spec.given(element("first").from(objects).orElse(null))
        .expect(Element::getValue, to().be(lastElementValue));
  }

}
