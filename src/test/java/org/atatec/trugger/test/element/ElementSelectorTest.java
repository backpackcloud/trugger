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
import org.atatec.trugger.test.Should;
import org.atatec.trugger.test.TestScenario;
import org.junit.Test;

import java.util.function.Predicate;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementSelectorTest {

  private Finder<Element> finder;
  private Element element;

  public ElementSelectorTest() {
    finder = createMock(Finder.class);
    Result<Element, Object> result = target -> element;
    expect(finder.find("name")).andReturn(result).anyTimes();
    replay(finder);
  }

  private ElementSelector selector() {
    return new TruggerElementSelector("name", finder);
  }

  private void testPredicate(Predicate<? super Element> predicate) {
    TestScenario.given(selector())
        .the(selector -> selector.filter(predicate).in(this),
            Should.be(element))
        .the(selector -> selector.filter(predicate.negate()).in(this),
            Should.BE_NULL);
    verify(finder);
  }

  private void testFailPredicate(Predicate<? super Element> predicate) {
    TestScenario.given(selector())
        .the(selector -> selector.filter(predicate).in(this),
            Should.BE_NULL);
    verify(finder);
  }

  @Test
  public void testAnnotatedSelector() {
    element = mock(element().annotatedWith(Flag.class));
    testPredicate(annotatedWith(Flag.class));
    testPredicate(annotated());
  }

  @Test
  public void testReadableSelector() {
    element = mock(element().readable());
    testPredicate(readable());
  }

  @Test
  public void testSpecificSelector() {
    element = mock(element().specific());
    testPredicate(specific());
  }

  @Test
  public void testWritableSelector() {
    element = mock(element().writable());
    testPredicate(writable());
  }

  @Test
  public void testNonWritableSelector() {
    element = mock(element().nonWritable());
    testPredicate(writable().negate());
  }

  @Test
  public void testOfTypeSelector() {
    element = mock(element().ofType(String.class));
    testPredicate(type(String.class));
    testFailPredicate(type(Integer.class));
    testFailPredicate(type(CharSequence.class));

    element = mock(element().ofType(int.class));
    testPredicate(type(int.class));
    testFailPredicate(type(Integer.class));
  }

  @Test
  public void testAssignableToSelector() {
    element = mock(element().ofType(String.class));
    testPredicate(assignableTo(String.class));
    testPredicate(assignableTo(CharSequence.class));
    testFailPredicate(assignableTo(Integer.class));

    element = mock(element().ofType(int.class));
    testPredicate(assignableTo(Integer.class));
    testPredicate(assignableTo(int.class));
  }

}
