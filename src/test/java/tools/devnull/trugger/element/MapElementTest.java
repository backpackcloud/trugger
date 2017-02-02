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
import tools.devnull.kodo.Expectation;
import tools.devnull.kodo.Spec;
import tools.devnull.trugger.HandlingException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;
import static tools.devnull.trugger.TruggerTest.SIZE;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.writable;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MapElementTest implements ElementSpecs {

  @Test
  public void mapElementTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("key", "some value");

    Spec.given(element("key").in(map))
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(Map.class))
        .expect(Element::value, to().be("some value"))
        .when(valueIsSetTo("other value"))
        .expect(Element::value, to().be("other value"));

    map.put("other key", "other value");

    Spec.given(elements().in(map))
        .expect(SIZE, to().be(2))
        .expect(it(), to().have(elementsNamed("key", "other key")));

    assertTrue(elements().in(Map.class).isEmpty());
  }

  @Test
  public void testHandlingExceptions() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map = Collections.unmodifiableMap(map);

    Spec.given(element("none").in(map))
        .expect(attempToGetValue(), to().raise(HandlingException.class))
        .expect(attempToChangeValue(), to().raise(HandlingException.class))
        .expect(settingValueTo("value", "target"), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testNonSpecificElements() {
    Map<String, String> map = new HashMap<>();

    Spec.given(element("key").in(Map.class))
        .expect(it(), to().not().be(null))
        .expect(attempToGetValue(), to().raise(HandlingException.class))
        .when(valueIsSetTo("value", map))
        .expect(Expectation.value(map.get("key")), to().be("value"));
  }


}
