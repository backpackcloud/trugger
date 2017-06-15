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
import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.Result;
import tools.devnull.trugger.element.impl.TruggerElementSelector;
import tools.devnull.trugger.selector.ElementSelector;

import java.util.function.Predicate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.trugger.TruggerTest.element;
import static tools.devnull.trugger.element.ElementPredicates.annotated;
import static tools.devnull.trugger.element.ElementPredicates.annotatedWith;
import static tools.devnull.trugger.element.ElementPredicates.assignableTo;
import static tools.devnull.trugger.element.ElementPredicates.ofType;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.specific;
import static tools.devnull.trugger.element.ElementPredicates.writable;

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
    assertSame(element, selector().filter(predicate).in(this));
    assertNull(selector().filter(predicate.negate()).in(this));

    verify(finder, atLeastOnce()).find("name");
  }

  private void testFailPredicate(Predicate<? super Element> predicate) {
    assertNull(selector().filter(predicate).in(this));

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
