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

import org.atatec.trugger.HandlingException;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.SIZE;
import static org.kodo.Spec.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MapElementTest implements ElementSpecs {

  @Test
  public void mapElementTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("key", "some value");

    TestScenario.given(element("key").in(map))
        .it(should(be(readable())))
        .it(should(be(writable())))
        .the(type(), should(be(Object.class)))
        .the(declaringClass(), should(be(Map.class)))
        .the(value(), should(be("some value")))
        .when(valueIsSetTo("other value"))
        .the(value(), should(be("other value")));

    map.put("other key", "other value");

    TestScenario.given(elements().in(map))
        .the(SIZE, should(be(2)))
        .it(should(have(elementsNamed("key", "other key"))));

    TestScenario.given(elements().in(Map.class))
        .it(should(be(EMPTY)));
  }

  @Test
  public void testHandlingExceptions() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map = Collections.unmodifiableMap(map);

    TestScenario.given(element("none").in(map))
        .then(attempToGetValue(), should(raise(HandlingException.class)))
        .then(attempToChangeValue(), should(raise(HandlingException.class)))
        .then(settingValueTo("value", "target"), should(raise(IllegalArgumentException.class)));
  }

  @Test
  public void testNonSpecificElements() {
    Map<String, String> map = new HashMap<>();

    TestScenario.given(element("key").in(Map.class))
        .it(should(notBe(NULL)))
        .then(attempToGetValue(), should(raise(HandlingException.class)))
        .when(valueIsSetTo("value", map))
        .then(map.get("key"), should(be("value")));
  }


}
