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
package org.atatec.trugger.test;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.mock.MockBuilder;
import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * A builder for creating {@link Finder} mock objects for {@link Element
 * elements}.
 * <p>
 * This builder creates finders that don't care about the target.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementFinderMock implements MockBuilder<Finder<Element>> {

  private final Finder<Element> finder;
  private final Map<String, Element> elements;

  public ElementFinderMock() {
    finder = EasyMock.createMock(Finder.class);
    elements = new HashMap<>();
    expect(finder.findAll())
        .andReturn(target -> new ArrayList<>(elements.values())).anyTimes();
  }

  /**
   * Adds the elements for returning.
   *
   * @return a reference to this object.
   */
  public ElementFinderMock add(Element... elements) {
    for (Element element : elements) {
      this.elements.put(element.name(), element);
      expect(finder.find(element.name()))
          .andReturn(o -> element).anyTimes();
    }
    return this;
  }

  /**
   * Adds the mock for returning.
   *
   * @return a reference to this object.
   */
  public ElementFinderMock add(MockBuilder<Element> builder) {
    add(builder.createMock());
    return this;
  }

  @Override
  public Finder<Element> createMock() {
    replay(finder);
    return finder;
  }

}
