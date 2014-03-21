/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
import org.atatec.trugger.test.SelectionTest;
import org.junit.Test;

import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
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
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotated();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedAnnotatedSelector() {
    element = mock(element());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);

    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotated();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testReadableSelector() {
    element = mock(element().readable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.readable();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonReadable();
      }
    }, this);
  }

  @Test
  public void testNonReadableSelector() {
    element = mock(element().nonReadable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonReadable();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.readable();
      }
    }, this);
  }

  @Test
  public void testSpecificSelector() {
    element = mock(element().specific());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.specific();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonSpecific();
      }
    }, this);
  }

  @Test
  public void testNonSpecificSelector() {
    element = mock(element().nonSpecific());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonSpecific();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.specific();
      }
    }, this);
  }

  @Test
  public void testWritableSelector() {
    element = mock(element().writable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.writable();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonWritable();
      }
    }, this);
  }

  @Test
  public void testNonWritableSelector() {
    element = mock(element().nonWritable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonWritable();
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.writable();
      }
    }, this);
  }

  @Test
  public void testOfTypeSelector() {
    element = mock(element().ofType(String.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(String.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(Integer.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(CharSequence.class);
      }
    }, this);

    element = mock(element().ofType(int.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(int.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(Integer.class);
      }
    }, this);
  }

  @Test
  public void testAssignableToSelector() {
    element = mock(element().ofType(String.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.assignableTo(CharSequence.class);
      }
    }, this);
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.assignableTo(String.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.assignableTo(StringBuilder.class);
      }
    }, this);

    element = mock(element().ofType(int.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.assignableTo(Integer.class);
      }
    }, this);
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.assignableTo(int.class);
      }
    }, this);
  }

  private class Selection implements SelectionTest<ElementSelector, Element> {
    public ElementSelector createSelector() {
      return selector();
    }
    public void assertions(Element element) {
      assertSame(ElementSelectorTest.this.element, element);
    }
    public void makeSelections(ElementSelector selector) {}
  }

}
