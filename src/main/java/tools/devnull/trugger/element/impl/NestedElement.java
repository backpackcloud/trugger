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
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class to handle a path of {@link tools.devnull.trugger.element.Element properties}.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public final class NestedElement extends AbstractElement implements Element {

  private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

  /** the access path */
  private final List<Element> path;
  private final Object target;

  NestedElement(String name, List<Element> path, Object target) {
    super(name);
    this.path = path;
    this.target = target;
    this.annotatedElement = getLast();
  }

  @Override
  public Class<?> declaringClass() {
    return getFirst().declaringClass();
  }

  @Override
  public Class<?> type() {
    return getLast().type();
  }

  public boolean isReadable() {
    for (Element property : path) {
      if (!property.isReadable()) {
        return false;
      }
    }
    return true;
  }

  public boolean isWritable() {
    if (path.size() > 1) {
      // tests every element on the path to see if they are readable
      for (Element property : path.subList(0, path.size() - 1)) {
        // if the element is not readable, then the path to the last
        // element is not reachable
        if (!property.isReadable()) {
          return false;
        }
      }
    }
    return getLast().isWritable();
  }

  @Override
  public <E> E target() {
    return (E) target;
  }

  @Override
  public ValueHandler on(final Object target) {
    return new ValueHandler(){

      public <E> E getValue() throws HandlingException {
      Object value = target;
      for (Element property : getPath()) {
        value = property.on(value).getValue();
      }
      return (E) value;
    }

    public void setValue(Object value) throws HandlingException {
      Object _source = target;
      Element p;
      for (int i = 0 ; ;) {
        p = NestedElement.this.get(i);
        if (++i < getPath().size()) {
          _source = p.on(_source).getValue();
        } else {
          p.on(_source).setValue(value);
          break;
        }
      }
    }};
  }

  /**
   * @return the path
   */
  public final List<Element> getPath() {
    return path;
  }

  /**
   * @return the first property in the path
   */
  public final Element getFirst() {
    return path.get(0);
  }

  /**
   * @param index
   *          the index
   * @return the property in the specified path index
   */
  public final Element get(int index) {
    return path.get(index);
  }

  /**
   * @return the last property in the path
   */
  public final Element getLast() {
    return path.get(path.size() - 1);
  }

  @Override
  public boolean isSpecific() {
    return getLast().isSpecific();
  }

  static NestedElement createNestedElement(Object source, String elementsPath) {
    String[] names = DOT_PATTERN.split(elementsPath);
    List<Element> path = new ArrayList<Element>(names.length);
    Element element;
    Object _source = source;
    for (String string : names) {
      element = Elements.element(string).from(_source).result();
      if (element == null) {
        return null;
      }
      path.add(element);
      _source = element.isSpecific() ? element.getValue() : element.type();
      //if the next source is null, the element will no longer be specific
      if (_source == null) {
        _source = element.type(); //use the type instead of the value
      }
    }
    return new NestedElement(elementsPath, path, source);
  }

}
