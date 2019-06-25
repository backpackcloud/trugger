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
package io.backpackcloud.trugger.element;

import io.backpackcloud.trugger.HandlingException;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static io.backpackcloud.kodo.Expectation.it;
import static io.backpackcloud.kodo.Expectation.the;
import static io.backpackcloud.kodo.Expectation.to;
import static io.backpackcloud.trugger.element.ElementPredicates.readable;
import static io.backpackcloud.trugger.element.ElementPredicates.writable;
import static io.backpackcloud.trugger.element.Elements.element;
import static io.backpackcloud.trugger.element.Elements.elements;

/**
 * @author Marcelo Guimaraes
 */
public class PropertiesElementTest implements ElementExpectations {

  @Test
  public void propertiesElementTest() {
    assertTrue(elements().from(Properties.class).isEmpty());

    Properties properties = new Properties();
    properties.setProperty("login", "admin");
    properties.setProperty("password", "admin");

    Spec.given(elements().from(properties))
        .expect(it(), to().have(elementsNamed("login", "password")));

    Spec.given(element("login").from(properties).orElse(null))
        .expect(Element::type, to().be(String.class))
        .expect(Element::name, to().be("login"))
        .expect(Element::getValue, to().be("admin"))
        .expect(Element::declaringClass, to().be(Properties.class))

        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))

        .when(valueIsSetTo("guest"))
        .expect(Element::getValue, to().be("guest"))

        .expect(the(properties.getProperty("login")), to().be("guest"))

        .expect(settingValueTo(new Object()), to().raise(HandlingException.class))
        .expect(settingValueTo("", new Object()), to().raise(IllegalArgumentException.class));
  }
}
