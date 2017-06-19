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

import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.kodo.Expectation.*;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResourceBundleElementTest implements ElementExpectations {

  private ResourceBundle bundle =
      ResourceBundle.getBundle("tools.devnull.trugger.element.bundle");

  @Test
  public void testElement() {
    Spec.given(element("foo").from(bundle).result())
        .expect(Element::name, to().be("foo"))
        .expect(Element::getValue, to().be("bar"))
        .expect(Element::declaringClass, to().be(ResourceBundle.class))
        .expect(it(), to().be(readable()))
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().be(specific()))
        .expect(attempToChangeValue(), to().raise(HandlingException.class))
        .expect(gettingValueIn(new Object()),
            to().raise(IllegalArgumentException.class));

    Spec.given(element("foo").from(ResourceBundle.class).result())
        .expect(Element::name, to().be("foo"))
        .expect(Element::declaringClass, to().be(ResourceBundle.class))
        .expect(it(), to().be(readable()))
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().not().be(specific()))
        .expect(attempToChangeValue(), to().raise(HandlingException.class))
        .expect(gettingValue(), to().raise(HandlingException.class));

    Spec.given(element("not-present").from(bundle).result())
        .expect(attempToGetValue(), to().raise(HandlingException.class));
  }

  @Test
  public void testElements() {
    List<Element> elements = elements().from(bundle);
    assertFalse(elements.isEmpty());
    assertTrue(elementsNamed("foo", "framework", "author").test(elements));

    assertTrue(elements().from(ResourceBundle.class).isEmpty());
  }

}
