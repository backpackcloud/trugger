/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.element;

import static net.sf.trugger.test.TruggerTest.assertResult;
import static net.sf.trugger.util.mock.Mock.element;
import static net.sf.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.TruggerNoNamedElementSelector;
import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTest;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NoNamedElementSelectorTest {

  private Finder<Element> finder;
  private Element elementA;
  private Element elementB = mock(element());

  public NoNamedElementSelectorTest() {
    finder = createMock(Finder.class);
    Result<Set<Element>, Object> result = new Result<Set<Element>, Object>() {
      public Set<Element> in(Object target) {
        Set<Element> result = new HashSet<Element>();
        result.add(elementA);
        result.add(elementB);
        return result;
      }
    };
    expect(finder.findAll()).andReturn(result).anyTimes();
    replay(finder);
  }

  private ElementSelector selector() {
    return new TruggerNoNamedElementSelector(finder);
  }

  @Test
  public void testAnnotatedSelector() {
    elementA = mock(element().annotatedWith(Flag.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedSelector() {
    elementA = mock(element());
    elementB = mock(element().annotatedWith(Flag.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);

    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testReadableSelector() {
    elementA = mock(element().readable());
    elementB = mock(element().nonReadable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.readable();
      }
    }, this);
  }

  @Test
  public void testNonReadableSelector() {
    elementA = mock(element().nonReadable());
    elementB = mock(element().readable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonReadable();
      }
    }, this);
  }

  @Test
  public void testSpecificSelector() {
    elementA = mock(element().specific());
    elementB = mock(element().nonSpecific());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.specific();
      }
    }, this);
  }

  @Test
  public void testNonSpecificSelector() {
    elementA = mock(element().nonSpecific());
    elementB = mock(element().specific());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonSpecific();
      }
    }, this);
  }

  @Test
  public void testWritableSelector() {
    elementA = mock(element().writable());
    elementB = mock(element().nonWritable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.writable();
      }
    }, this);
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.forBind();
      }
    }, this);
  }

  @Test
  public void testNonWritableSelector() {
    elementA = mock(element().nonWritable());
    elementB = mock(element().writable());
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.nonWritable();
      }
    }, this);
  }

  @Test
  public void testOfTypeSelector() {
    elementA = mock(element().ofType(String.class));
    elementB = mock(element().ofType(Object.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(String.class);
      }
    }, this);

    elementA = mock(element().ofType(int.class));
    assertResult(new Selection() {
      public void makeSelections(ElementSelector selector) {
        selector.ofType(int.class);
      }
    }, this);
  }

  @Test
  public void testAssignableToSelector() {
    elementA = mock(element().ofType(String.class));
    elementB = mock(element().ofType(Object.class));
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

    elementA = mock(element().ofType(int.class));
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
      assertSame(elementA, element);
    }
    public void makeSelections(ElementSelector selector) {}
  }

  @Test(expected = SearchException.class)
  public void testInvalidSelection() {
    elementA = mock(element().writable());
    elementB = mock(element().writable());

    selector().writable().in(this);
  }

}
