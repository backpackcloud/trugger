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

import org.junit.Before;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.Arrays;
import java.util.List;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.SIZE;
import static org.kodo.Scenario.should;
import static org.kodo.Spec.be;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ListElementTest implements ElementSpecs {

  private List<Integer> ints;

  private List<TestObject> objects;

  @Before
  public void initialize() {
    ints = Arrays.asList(0, 10, 12, 33);
    objects = Arrays.asList(new TestObject("name", "lastname"));
  }

  @Test
  public void testFindingAll() {
    TestScenario.given(elements().in(ints))
        .the(SIZE, should(be(4)))
        .the(elementAt(0), should(be(0)))
        .the(elementAt(1), should(be(10)))
        .the(elementAt(2), should(be(12)))
        .the(elementAt(3), should(be(33)));
  }

  @Test
  public void testIndexElements() {
    TestScenario.given(element("0").in(ints))
        .it(should(be(readable())))
        .it(should(be(writable())))
        .the(type(), should(be(Object.class)))
        .the(declaringClass(), should(be(List.class)))
        .the(value(), should(be(0)))

        .when(valueIsSetTo(15))
        .the(value(), should(be(15)));

    TestScenario.given(element("2").in(ints))
        .the(type(), should(be(Object.class)))
        .the(declaringClass(), should(be(List.class)))
        .the(value(), should(be(12)));

    TestScenario.given(element("ints.1").in(this))
        .the(type(), should(be(Object.class)))
        .the(declaringClass(), should(be(ListElementTest.class)))
        .the(value(), should(be(10)));

    TestScenario.given(element("0").in(objects))
        .the(type(), should(be(Object.class)))
        .the(declaringClass(), should(be(List.class)))
        .the(valueOf("name"), should(be("name")))
        .the(valueOf("lastName"), should(be("lastname")));
  }

  @Test
  public void testReferencedElements() {
    TestScenario.given(element("first").in(ints))
        .the(value(), should(be(0)));

    TestScenario.given(element("last").in(ints))
        .the(value(), should(be(33)));

    Object lastElementValue = element("last").in(objects).value();
    TestScenario.given(element("first").in(objects))
        .the(value(), should(be(lastElementValue)));
  }

}
