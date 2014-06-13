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

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.element.impl.TruggerElementsSelector;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.test.Flag;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.function.Predicate;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.atatec.trugger.test.TruggerTest.elementFinder;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.verify;
import static org.kodo.Spec.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementsSelectorTest implements ElementSpecs {

  private Finder<Element> finder;

  private ElementsSelector selector() {
    return new TruggerElementsSelector(finder);
  }

  private void testPredicate(Predicate<? super Element> predicate,
                             String... names) {
    TestScenario.given(selector().filter(predicate).in(this))
        .it(should(have(elementsNamed(names))));
    verify(finder);
  }

  private void testFailPredicate(Predicate<? super Element> predicate) {
    TestScenario.given(selector())
        .the(selector -> selector.filter(predicate).in(this),
            should(be(EMPTY)));
    verify(finder);
  }

  @Test
  public void testNoSelector() {
    finder = mock(elementFinder()
        .add(element().named("foo"))
        .add(element().named("bar")));
    testPredicate(el -> true, "foo", "bar");
  }

  @Test
  public void testAnnotatedSelector() {
    finder = mock(elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")));
    testPredicate(annotatedWith(Flag.class), "annotated");
    testPredicate(annotated(), "annotated");
  }

  @Test
  public void testReadableSelector() {
    finder = mock(elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()));
    testPredicate(readable(), "readable");
  }

  @Test
  public void testSpecificSelector() {
    finder = mock(elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()));
    testPredicate(specific(), "specific");
  }

  @Test
  public void testWritableSelector() {
    finder = mock(elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable()));
    testPredicate(writable(), "writable");
  }

  @Test
  public void testTypeSelector() {
    finder = mock(elementFinder()
        .add(element().named("string").ofType(String.class))
        .add(element().named("stringBuilder").ofType(StringBuilder.class))
        .add(element().named("integer").ofType(Integer.class)));
    testPredicate(ElementPredicates.type(String.class), "string");
    testPredicate(ElementPredicates.type(Integer.class), "integer");
    testFailPredicate(ElementPredicates.type(CharSequence.class));
    testPredicate(assignableTo(CharSequence.class), "string", "stringBuilder");
  }

  @Test
  public void testNamedSelector() {
    finder = mock(elementFinder()
        .add(element().named("elementA"))
        .add(element().named("elementB"))
        .add(element().named("elementC")));
    testPredicate(named("elementA", "elementC", "elementD"),
        "elementA", "elementC");
  }

}
