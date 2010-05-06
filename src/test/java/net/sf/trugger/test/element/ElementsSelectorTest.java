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

import static net.sf.trugger.test.TruggerTest.assertElements;
import static net.sf.trugger.test.TruggerTest.assertNoResult;
import static net.sf.trugger.test.TruggerTest.assertResult;
import static net.sf.trugger.util.mock.Mock.element;
import static net.sf.trugger.util.mock.Mock.elementFinder;
import static net.sf.trugger.util.mock.Mock.mock;

import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.TruggerElementsSelector;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTest;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementsSelectorTest {

  private Finder<Element> finder;

  private ElementsSelector selector() {
    return new TruggerElementsSelector(finder);
  }

  @Test
  public void testNoSelector() {
    finder = mock(elementFinder()
        .add(element().named("foo"))
        .add(element().named("bar")));

    assertResult(new Selection("foo", "bar"), this);
  }

  @Test
  public void testAnnotatedSelector() {
    finder = mock(elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")));

    assertResult(new Selection("annotated") {
      public void makeSelections(ElementsSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
    assertResult(new Selection("annotated") {
      public void makeSelections(ElementsSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedAnnotatedSelector() {
    finder = mock(elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")));

    assertResult(new Selection("notAnnotated") {
      public void makeSelections(ElementsSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
    assertResult(new Selection("notAnnotated") {
      public void makeSelections(ElementsSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testReadableSelector() {
    finder = mock(elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()));

    assertResult(new Selection("readable") {
      public void makeSelections(ElementsSelector selector) {
        selector.readable();
      }
    }, this);
  }

  @Test
  public void testNonReadableSelector() {
    finder = mock(elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()));

    assertResult(new Selection("nonReadable") {
      public void makeSelections(ElementsSelector selector) {
        selector.nonReadable();
      }
    }, this);
  }

  @Test
  public void testSpecificSelector() {
    finder = mock(elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()));

    assertResult(new Selection("specific") {
      public void makeSelections(ElementsSelector selector) {
        selector.specific();
      }
    }, this);
  }

  @Test
  public void testNonSpecificSelector() {
    finder = mock(elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()));

    assertResult(new Selection("nonSpecific") {
      public void makeSelections(ElementsSelector selector) {
        selector.nonSpecific();
      }
    }, this);
  }

  @Test
  public void testWritableSelector() {
    finder = mock(elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable()));

    assertResult(new Selection("writable") {
      public void makeSelections(ElementsSelector selector) {
        selector.writable();
      }
    }, this);
  }

  @Test
  public void testNonWritableSelector() {
    finder = mock(elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable()));

    assertResult(new Selection("nonWritable") {
      public void makeSelections(ElementsSelector selector) {
        selector.nonWritable();
      }
    }, this);
  }

  @Test
  public void testOfTypeSelector() {
    finder = mock(elementFinder()
        .add(element().named("string").ofType(String.class))
        .add(element().named("stringBuilder").ofType(StringBuilder.class))
        .add(element().named("integer").ofType(Integer.class)));

    assertResult(new Selection("string") {
      public void makeSelections(ElementsSelector selector) {
        selector.ofType(String.class);
      }
    }, this);
    assertResult(new Selection("integer") {
      public void makeSelections(ElementsSelector selector) {
        selector.ofType(Integer.class);
      }
    }, this);
    assertNoResult(new Selection() {
      public void makeSelections(ElementsSelector selector) {
        selector.ofType(CharSequence.class);
      }
    }, this);
    assertResult(new Selection("string", "stringBuilder") {
      public void makeSelections(ElementsSelector selector) {
        selector.assignableTo(CharSequence.class);
      }
    }, this);
  }

  @Test
  public void testNamedSelector() {
    finder = mock(elementFinder()
        .add(element().named("elementA"))
        .add(element().named("elementB"))
        .add(element().named("elementC")));

    assertResult(new Selection("elementA", "elementC") {
      public void makeSelections(ElementsSelector selector) {
        selector.named("elementA", "elementC", "elementD");
      }
    }, this);
  }

  private class Selection implements SelectionTest<ElementsSelector, Set<Element>> {
    private final String[] names;

    public Selection(String... names) {
      this.names = names;
    }
    public ElementsSelector createSelector() {
      return selector();
    }
    public void assertions(Set<Element> elements) {
      assertElements(elements, names);
    }
    public void makeSelections(ElementsSelector selector) {}
  }

}
