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

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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

  @Test
  public void testAnnotatedSelector() {
    element = mock(element().annotatedWith(Flag.class));
    TestScenario.given(selector())
        .the(selector -> selector.filter(annotatedWith(Flag.class)).in(this),
            Should.be(element))
        .the(selector -> selector.filter(annotated()).in(this),
            Should.be(element))
        .the(selector -> selector.filter(annotatedWith(Flag.class).negate()).in(this),
            Should.BE_NULL)
        .the(selector -> selector.filter(annotated().negate()).in(this),
            Should.BE_NULL);
  }

  @Test
  public void testReadableSelector() {
    element = mock(element().readable());
    assertSame(
        element,
        selector().filter(readable()).in(this)
    );
  }

  @Test
  public void testSpecificSelector() {
    element = mock(element().specific());
    assertSame(
        element,
        selector().filter(specific()).in(this)
    );
  }

  @Test
  public void testWritableSelector() {
    element = mock(element().writable());
    assertSame(
        element,
        selector().filter(writable()).in(this)
    );
  }

  @Test
  public void testNonWritableSelector() {
    element = mock(element().nonWritable());
    assertNull(
        selector().filter(writable()).in(this)
    );
  }

  @Test
  public void testOfTypeSelector() {
    element = mock(element().ofType(String.class));
    assertSame(
        element,
        selector().filter(type(String.class)).in(this)
    );
    assertNull(
        selector().filter(type(Integer.class)).in(this)
    );
    assertNull(
        selector().filter(type(CharSequence.class)).in(this)
    );

    element = mock(element().ofType(int.class));
    assertSame(
        element,
        selector().filter(type(int.class)).in(this)
    );
    assertNull(
        selector().filter(type(Integer.class)).in(this)
    );
  }

  @Test
  public void testAssignableToSelector() {
    element = mock(element().ofType(String.class));
    assertSame(
        element,
        selector().filter(assignableTo(String.class)).in(this)
    );
    assertNull(
        selector().filter(assignableTo(Integer.class)).in(this)
    );
    assertSame(
        element,
        selector().filter(assignableTo(CharSequence.class)).in(this)
    );

    element = mock(element().ofType(int.class));
    assertSame(
        element,
        selector().filter(assignableTo(Integer.class)).in(this)
    );
    assertSame(
        element,
        selector().filter(assignableTo(int.class)).in(this)
    );
  }

}
