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

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.formatter.Formatter;
import net.sf.trugger.formatter.FormatterFactory;

/**
 * The element for a component such as visual component (Swing, AWT or SWT) or
 * predefined ones.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public abstract class ComponentElement<T> extends DecoratedElement {

  protected final FormatterFactory factory;

  public ComponentElement(Element decorated) {
    super(decorated);
    factory = new FormatterFactory();
  }

  @Override
  public boolean isWritable() {
    return true;
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public ValueHandler in(final Object target) {
    return new ValueHandler() {

      @Override
      public void value(Object value) throws HandlingException {
        T component = element.in(target).value();
        setComponentValue(component, value);
      }

      @Override
      public <E> E value() throws HandlingException {
        T component = element.in(target).value();
        return (E) getComponentValue(component);
      }

    };
  }

  /**
   * Formats the given value using the element annotations.
   *
   * @param value
   *          the value to format
   * @return the formatted value.
   */
  protected String format(Object value) {
    Formatter formatter = factory.create(element);
    return formatter.format(value);
  }

  /**
   * Parses the given value using the element annotations.
   *
   * @param value
   *          the value to parse
   * @return the parsed value.
   */
  protected Object parse(String value) {
    Formatter formatter = factory.create(element);
    return formatter.parse(value);
  }

  protected abstract Object getComponentValue(T component);

  protected abstract void setComponentValue(T component, Object value);

}
