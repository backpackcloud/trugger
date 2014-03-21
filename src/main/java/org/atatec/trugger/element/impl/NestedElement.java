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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.util.HashBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class to handle a path of {@link org.atatec.trugger.element.Element properties}.
 *
 * @author Marcelo Guimarães
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
      for (Element property : path.subList(0, path.size() - 1)) {
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
  public ValueHandler in(final Object target) {
    return new ValueHandler(){

      public <E> E value() throws HandlingException {
      Object value = target;
      for (Element property : getPath()) {
        value = property.in(value).value();
      }
      return (E) value;
    }

    public void value(Object value) throws HandlingException {
      Object _source = target;
      Element p;
      for (int i = 0 ; ;) {
        p = NestedElement.this.get(i);
        if (++i < getPath().size()) {
          _source = p.in(_source).value();
        } else {
          p.in(_source).value(value);
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

  @Override
  public int hashCode() {
    return new HashBuilder(path).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final NestedElement other = (NestedElement) obj;
    return path.equals(other.path);
  }

  static NestedElement createNestedElement(Object source, String elementsPath) {
    String[] names = DOT_PATTERN.split(elementsPath);
    List<Element> path = new ArrayList<Element>(names.length);
    Element property;
    Object _source = source;
    for (String string : names) {
      property = Elements.element(string).in(_source);
      path.add(property);
      _source = property.isSpecific() ? property.value() : property.type();
      //if the next source is null, the property will no longer be specific
      if (_source == null) {
        _source = property.type(); //use the type instead of the value
      }
    }
    return new NestedElement(elementsPath, path, source);
  }

}
