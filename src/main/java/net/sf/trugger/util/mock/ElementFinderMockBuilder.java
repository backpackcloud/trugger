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
package net.sf.trugger.util.mock;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;

/**
 * A buider for creating {@link Finder} mock objects for {@link Element
 * elements}.
 * <p>
 * This builder creates finders that don't care about the target.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementFinderMockBuilder implements MockBuilder<Finder<Element>> {
  
  private final Finder<Element> finder;
  private final Map<String, Element> elements;
  
  public ElementFinderMockBuilder() {
    finder = createMock(Finder.class);
    elements = new HashMap<String, Element>();
    expect(finder.findAll()).andReturn(new ElementsResult()).anyTimes();
  }
  
  /**
   * Adds the elements for returning.
   * 
   * @return a reference to this object.
   */
  public ElementFinderMockBuilder add(Element... elements) {
    for (Element element : elements) {
      this.elements.put(element.name(), element);
      expect(finder.find(element.name())).andReturn(new ElementResult(element)).anyTimes();
    }
    return this;
  }
  
  /**
   * Adds the mock for returning.
   * 
   * @return a reference to this object.
   */
  public ElementFinderMockBuilder add(MockBuilder<Element> builder) {
    add(builder.mock());
    return this;
  }
  
  @Override
  public Finder<Element> mock() {
    replay(finder);
    return finder;
  }
  
  private static class ElementResult implements Result<Element, Object> {
    
    private final Element result;
    
    private ElementResult(Element result) {
      this.result = result;
    }
    
    @Override
    public Element in(Object target) {
      return result;
    }
  }
  
  private class ElementsResult implements Result<Set<Element>, Object> {
    
    @Override
    public Set<Element> in(Object target) {
      return new HashSet<Element>(elements.values());
    }
    
  }
  
}
