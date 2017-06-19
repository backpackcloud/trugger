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
import tools.devnull.trugger.Optional;
import tools.devnull.trugger.element.impl.TruggerElementSelector;
import tools.devnull.trugger.selector.ElementSelector;

import java.util.function.Predicate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementSelectorTest {

  private ElementFinder finder;
  private ElementSelector selector;
  private Element element;
  private Predicate<Element> all;
  private Predicate<Element> none;
  private String elementName;

  @Before
  public void initialize() {
    finder = mock(ElementFinder.class);
    element = mock(Element.class);
    all = mock(Predicate.class);
    none = mock(Predicate.class);
    elementName = "name";

    when(finder.find(elementName, this)).thenReturn(Optional.of(element));
    when(all.test(any(Element.class))).thenReturn(true);
    when(none.test(any(Element.class))).thenReturn(false);
    selector = new TruggerElementSelector(elementName, finder);
  }

  @Test
  public void testWithoutPredicate() {
    Element result = selector.from(this);
    assertSame(element, result);
    verify(finder).find(elementName, this);
  }

  @Test
  public void testWithPredicate() {
    Element result = selector.filter(all).from(this);
    assertSame(element, result);
    verify(all).test(element);

    result = selector.filter(none).from(this);
    assertNull(result);
    verify(none).test(element);
  }

}
