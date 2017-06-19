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

import org.junit.Test;
import tools.devnull.kodo.Spec;
import tools.devnull.trugger.HandlingException;

import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.writable;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertiesElementTest implements ElementSpecs {

  @Test
  public void propertiesElementTest() {
    assertTrue(elements().from(Properties.class).isEmpty());

    Properties properties = new Properties();
    properties.setProperty("login", "admin");
    properties.setProperty("password", "admin");

    Spec.given(elements().from(properties))
        .expect(it(), to().have(elementsNamed("login", "password")));

    Spec.given(element("login").from(properties))
        .expect(Element::type, to().be(String.class))
        .expect(Element::name, to().be("login"))
        .expect(Element::get, to().be("admin"))
        .expect(Element::declaringClass, to().be(Properties.class))

        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))

        .when(valueIsSetTo("guest"))
        .expect(Element::get, to().be("guest"))

        .expect(the(properties.getProperty("login")), to().be("guest"))

        .expect(settingValueTo(new Object()), to().raise(HandlingException.class))
        .expect(settingValueTo("", new Object()), to().raise(IllegalArgumentException.class));
  }
}
