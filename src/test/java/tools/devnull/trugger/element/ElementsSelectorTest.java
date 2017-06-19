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

import org.junit.Before;
import org.junit.Test;
import tools.devnull.trugger.element.impl.TruggerElementsSelector;
import tools.devnull.trugger.selector.ElementsSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementsSelectorTest implements ElementExpectations {

  private ElementFinder finder;
  private ElementsSelector selector;
  private Element element;
  private Predicate<Element> all;
  private Predicate<Element> none;
  private List<Element> response;

  @Before
  public void initialize() {
    finder = mock(ElementFinder.class);
    element = mock(Element.class);
    all = mock(Predicate.class);
    none = mock(Predicate.class);
    response = new ArrayList<>();

    when(finder.findAll(this)).thenReturn(response);
    when(all.test(any(Element.class))).thenReturn(true);
    when(none.test(any(Element.class))).thenReturn(false);
    response.add(element);
    selector = new TruggerElementsSelector(finder);
  }

  @Test
  public void testWithoutPredicate() {
    List<Element> result = selector.from(this);
    assertSame(response, result);
    verify(finder).findAll(this);
  }

  @Test
  public void testWithPredicate() {
    List<Element> result = selector.filter(all).from(this);
    assertEquals(1, result.size());
    assertEquals(element, result.get(0));
    verify(all).test(element);

    result = selector.filter(none).from(this);
    assertTrue(result.isEmpty());
    verify(none).test(element);
  }

}
