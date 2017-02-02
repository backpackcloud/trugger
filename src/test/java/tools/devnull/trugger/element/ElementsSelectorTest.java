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
import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.element.impl.TruggerElementsSelector;
import tools.devnull.trugger.selector.ElementsSelector;

import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.TruggerTest.element;
import static tools.devnull.trugger.TruggerTest.elementFinder;
import static tools.devnull.trugger.element.ElementPredicates.annotated;
import static tools.devnull.trugger.element.ElementPredicates.annotatedWith;
import static tools.devnull.trugger.element.ElementPredicates.assignableTo;
import static tools.devnull.trugger.element.ElementPredicates.named;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.specific;
import static tools.devnull.trugger.element.ElementPredicates.writable;

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
    assertTrue(elementsNamed(names).test(selector().filter(predicate).in(this)));
  }

  private void testFailPredicate(Predicate<? super Element> predicate) {
    assertTrue(selector().filter(predicate).in(this).isEmpty());
  }

  @Test
  public void testNoSelector() {
    finder = elementFinder()
        .add(element().named("foo"))
        .add(element().named("bar"))
        .createMock();
    testPredicate(el -> true, "foo", "bar");
  }

  @Test
  public void testAnnotatedSelector() {
    finder = elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")).createMock();
    testPredicate(annotatedWith(Flag.class), "annotated");
    testPredicate(annotated(), "annotated");
  }

  @Test
  public void testReadableSelector() {
    finder = elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()).createMock();
    testPredicate(readable(), "readable");
  }

  @Test
  public void testSpecificSelector() {
    finder = elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()).createMock();
    testPredicate(specific(), "specific");
  }

  @Test
  public void testWritableSelector() {
    finder = elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable())
        .createMock();
    testPredicate(writable(), "writable");
  }

  @Test
  public void testTypeSelector() {
    finder = elementFinder()
        .add(element().named("string").ofType(String.class))
        .add(element().named("stringBuilder").ofType(StringBuilder.class))
        .add(element().named("integer").ofType(Integer.class))
        .createMock();
    testPredicate(ElementPredicates.ofType(String.class), "string");
    testPredicate(ElementPredicates.ofType(Integer.class), "integer");
    testFailPredicate(ElementPredicates.ofType(CharSequence.class));
    testPredicate(assignableTo(CharSequence.class), "string", "stringBuilder");
  }

  @Test
  public void testNamedSelector() {
    finder = elementFinder()
        .add(element().named("elementA"))
        .add(element().named("elementB"))
        .add(element().named("elementC"))
        .createMock();
    testPredicate(named("elementA", "elementC", "elementD"),
        "elementA", "elementC");
  }

}
