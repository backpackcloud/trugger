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
import tools.devnull.kodo.TestScenario;
import tools.devnull.trugger.HandlingException;

import java.util.ResourceBundle;

import static tools.devnull.kodo.Spec.*;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResourceBundleElementTest implements ElementSpecs {

  private ResourceBundle bundle =
      ResourceBundle.getBundle("tools.devnull.trugger.element.bundle");

  @Test
  public void testElement() {
    TestScenario.given(element("foo").in(bundle))
        .the(name(), should(be("foo")))
        .the(value(), should(be("bar")))
        .the(declaringClass(), should(be(ResourceBundle.class)))
        .it(should(be(readable())))
        .it(should(notBe(writable())))
        .it(should(be(specific())))
        .then(attempToChangeValue(), should(raise(HandlingException.class)))
        .then(gettingValueIn(new Object()),
            should(raise(IllegalArgumentException.class)));

    TestScenario.given(element("foo").in(ResourceBundle.class))
        .the(name(), should(be("foo")))
        .the(declaringClass(), should(be(ResourceBundle.class)))
        .it(should(be(readable())))
        .it(should(notBe(writable())))
        .it(should(notBe(specific())))
        .then(attempToChangeValue(), should(raise(HandlingException.class)))
        .then(gettingValue(), should(raise(HandlingException.class)));

    TestScenario.given(element("not-present").in(bundle))
        .then(attempToGetValue(), should(raise(HandlingException.class)));
  }

  @Test
  public void testElements() {
    TestScenario.given(elements().in(bundle))
        .it(should(notBe(EMPTY)))
        .it(should(have(elementsNamed("foo", "framework", "author"))));

    TestScenario.given(elements().in(ResourceBundle.class))
        .it(should(be(EMPTY)));
  }

}
