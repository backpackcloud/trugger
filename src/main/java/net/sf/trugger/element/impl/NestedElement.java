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
package net.sf.trugger.element.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;

/**
 * A class to handle a path of {@link Element properties}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class NestedElement extends AbstractElement implements Element {

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
    for (Element property : getPath()) {
      if (!property.isReadable()) {
        return false;
      }
    }
    return true;
  }

  public boolean isWritable() {
    if (path.size() > 1) {
      for (Element property : getPath().subList(0, path.size() - 1)) {
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
  public ValueHandler in(Object target) {
    return new NestedElementHandler(target);
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
    return getPath().get(0);
  }

  /**
   * @param index
   *          the index
   * @return the property in the specified path index
   */
  public final Element get(int index) {
    return getPath().get(index);
  }

  /**
   * @return the last property in the path
   */
  public final Element getLast() {
    return getPath().get(path.size() - 1);
  }

  @Override
  public boolean isSpecific() {
    return getLast().isSpecific();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + path.hashCode();
    return result;
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
    if (!path.equals(other.path)) {
      return false;
    }
    return true;
  }

  private class NestedElementHandler implements ValueHandler {

    private final Object source;

    public NestedElementHandler(Object source) {
      super();
      this.source = source;
    }

    public <E> E value() throws HandlingException {
      Object value = source;
      for (Element property : getPath()) {
        value = property.in(value).value();
      }
      return (E) value;
    }

    public void value(Object value) throws HandlingException {
      Object _source = source;
      Element p = null;
      for (int i = 0 ; ;) {
        p = NestedElement.this.get(i);
        if (++i < getPath().size()) {
          _source = p.in(_source).value();
        } else {
          p.in(_source).value(value);
          break;
        }
      }
    }

  }

  static NestedElement createNestedElement(Object source, String elementsPath) {
    String[] names = elementsPath.split("\\.");
    List<Element> path = new ArrayList<Element>(names.length);
    Element property = null;
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
