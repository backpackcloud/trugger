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

import com.backpackcloud.trugger.HandlingException;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static io.backpackcloud.kodo.Expectation.it;
import static io.backpackcloud.kodo.Expectation.the;
import static io.backpackcloud.kodo.Expectation.to;
import static com.backpackcloud.trugger.TruggerTest.SIZE;
import static com.backpackcloud.trugger.element.ElementPredicates.readable;
import static com.backpackcloud.trugger.element.ElementPredicates.writable;
import static com.backpackcloud.trugger.element.Elements.element;
import static com.backpackcloud.trugger.element.Elements.elements;

/**
 * @author Marcelo Guimaraes
 */
public class MapElementTest implements ElementExpectations {

  @Test
  public void mapElementTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("key", "some value");

    Spec.given(element("key").from(map).orElse(null))
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(Element::type, to().be(Object.class))
        .expect(Element::declaringClass, to().be(Map.class))
        .expect(Element::getValue, to().be("some value"))
        .when(valueIsSetTo("other value"))
        .expect(Element::getValue, to().be("other value"));

    map.put("other key", "other value");

    Spec.given(elements().from(map))
        .expect(SIZE, to().be(2))
        .expect(it(), to().have(elementsNamed("key", "other key")));

    assertTrue(elements().from(Map.class).isEmpty());
  }

  @Test
  public void testHandlingExceptions() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map = Collections.unmodifiableMap(map);

    Spec.given(element("none").from(map).orElse(null))
        .expect(getValue(), to().raise(HandlingException.class))
        .expect(attempToChangeValue(), to().raise(HandlingException.class))
        .expect(settingValueTo("value", "target"), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testNonSpecificElements() {
    Map<String, String> map = new HashMap<>();

    Spec.given(element("key").from(Map.class).orElse(null))
        .expect(it(), to().not().beNull())
        .expect(getValue(), to().raise(HandlingException.class))
        .when(valueIsSetTo("value", map))
        .expect(the(map.get("key")), to().be("value"));
  }


}
