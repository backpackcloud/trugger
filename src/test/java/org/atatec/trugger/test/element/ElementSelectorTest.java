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
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.TruggerElementSelector;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.test.Flag;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.kodo.Spec.*;
import static org.mockito.Mockito.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementSelectorTest {

  private Finder<Element> finder;
  private Element element;

  public ElementSelectorTest() {
    finder = mock(Finder.class);
    Result<Element, Object> result = target -> element;
    when(finder.find("name")).thenReturn(result);
  }

  private ElementSelector selector() {
    return new TruggerElementSelector("name", finder);
  }

  private void testPredicate(Predicate<? super Element> predicate) {
    Function<ElementSelector, Object> filtered =
        selector -> selector.filter(predicate).in(this);
    Function<ElementSelector, Object> inversedFiltered =
        selector -> selector.filter(predicate.negate()).in(this);

    TestScenario.given(selector())
        .the(filtered, should(be(element)))
        .the(inversedFiltered,
            should(be(NULL)));

    verify(finder, atLeastOnce()).find("name");
  }

  private void testFailPredicate(Predicate<? super Element> predicate) {
    TestScenario.given(selector())
        .the(selector -> selector.filter(predicate).in(this),
            should(be(NULL)));

    verify(finder, atLeastOnce()).find("name");
  }

  @Test
  public void testAnnotatedSelector() {
    element = element().annotatedWith(Flag.class).createMock();
    testPredicate(annotatedWith(Flag.class));
    testPredicate(annotated());
  }

  @Test
  public void testReadableSelector() {
    element = element().readable().createMock();
    testPredicate(readable());
  }

  @Test
  public void testSpecificSelector() {
    element = element().specific().createMock();
    testPredicate(specific());
  }

  @Test
  public void testWritableSelector() {
    element = element().writable().createMock();
    testPredicate(writable());
  }

  @Test
  public void testNonWritableSelector() {
    element = element().nonWritable().createMock();
    testPredicate(writable().negate());
  }

  @Test
  public void testOfTypeSelector() {
    element = element().ofType(String.class).createMock();
    testPredicate(ofType(String.class));
    testFailPredicate(ofType(Integer.class));
    testFailPredicate(ofType(CharSequence.class));

    element = element().ofType(int.class).createMock();
    testPredicate(ofType(int.class));
    testFailPredicate(ofType(Integer.class));
  }

  @Test
  public void testAssignableToSelector() {
    element = element().ofType(String.class).createMock();
    testPredicate(assignableTo(String.class));
    testPredicate(assignableTo(CharSequence.class));
    testFailPredicate(assignableTo(Integer.class));

    element = element().ofType(int.class).createMock();
    testPredicate(assignableTo(Integer.class));
    testPredicate(assignableTo(int.class));
  }

}
