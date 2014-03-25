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
import org.atatec.trugger.element.impl.TruggerElementsSelector;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.*;
import static org.atatec.trugger.util.mock.Mock.mock;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementsSelectorTest {

  private Finder<Element> finder;

  private ElementsSelector select() {
    return new TruggerElementsSelector(finder);
  }

  @Test
  public void testNoSelector() {
    finder = mock(elementFinder()
        .add(element().named("foo"))
        .add(element().named("bar")));
    assertElements(select().in(this), "foo", "bar");
  }

  @Test
  public void testAnnotatedSelector() {
    finder = mock(elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")));

    assertElements(
        select()
            .filter(annotatedWith(Flag.class))
            .in(this)
        , "annotated"
    );
    assertElements(
        select()
            .filter(ANNOTATED)
            .in(this)
        , "annotated"
    );
  }

  @Test
  public void testNotAnnotatedSelector() {
    finder = mock(elementFinder()
        .add(element().named("annotated").annotatedWith(Flag.class))
        .add(element().named("notAnnotated")));

    assertElements(
        select()
            .filter(notAnnotatedWith(Flag.class))
            .in(this)
        , "notAnnotated"
    );
    assertElements(
        select()
            .filter(NOT_ANNOTATED)
            .in(this)
        , "notAnnotated"
    );
  }

  @Test
  public void testReadableSelector() {
    finder = mock(elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()));

    assertElements(
        select()
            .filter(READABLE)
            .in(this)
        , "readable"
    );
  }

  @Test
  public void testNonReadableSelector() {
    finder = mock(elementFinder()
        .add(element().named("readable").readable())
        .add(element().named("nonReadable").nonReadable()));

    assertElements(
        select()
            .filter(NON_READABLE)
            .in(this)
        , "nonReadable"
    );
  }

  @Test
  public void testSpecificSelector() {
    finder = mock(elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()));

    assertElements(
        select()
            .filter(SPECIFIC)
            .in(this)
        , "specific"
    );
  }

  @Test
  public void testNonSpecificSelector() {
    finder = mock(elementFinder()
        .add(element().named("specific").specific())
        .add(element().named("nonSpecific").nonSpecific()));

    assertElements(
        select()
            .filter(NON_SPECIFIC)
            .in(this)
        , "nonSpecific"
    );
  }

  @Test
  public void testWritableSelector() {
    finder = mock(elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable()));

    assertElements(
        select()
            .filter(WRITABLE)
            .in(this)
        , "writable"
    );
  }

  @Test
  public void testNonWritableSelector() {
    finder = mock(elementFinder()
        .add(element().named("writable").writable())
        .add(element().named("nonWritable").nonWritable()));

    assertElements(
        select()
            .filter(NON_WRITABLE)
            .in(this)
        , "nonWritable"
    );
  }

  @Test
  public void testOfTypeSelector() {
    finder = mock(elementFinder()
        .add(element().named("string").ofType(String.class))
        .add(element().named("stringBuilder").ofType(StringBuilder.class))
        .add(element().named("integer").ofType(Integer.class)));

    assertElements(
        select()
            .filter(ofType(String.class))
            .in(this)
        , "string"
    );
    assertElements(
        select()
            .filter(ofType(Integer.class))
            .in(this)
        , "integer"
    );
    assertTrue(
        select()
            .filter(ofType(CharSequence.class))
            .in(this)
        .isEmpty()
    );

    assertElements(
        select()
            .filter(assignableTo(CharSequence.class))
            .in(this)
        , "string", "stringBuilder"
    );
  }

  @Test
  public void testNamedSelector() {
    finder = mock(elementFinder()
        .add(element().named("elementA"))
        .add(element().named("elementB"))
        .add(element().named("elementC")));

    assertElements(
        select()
            .filter(named("elementA", "elementC", "elementD"))
            .in(this)
        , "elementA", "elementC"
    );
  }

}
