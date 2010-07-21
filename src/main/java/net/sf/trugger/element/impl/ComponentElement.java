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
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementValueHandler;
import net.sf.trugger.factory.AnnotationFactoryContextImpl;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.format.Formatters;
import net.sf.trugger.transformer.BidirectionalTransformer;
import net.sf.trugger.transformer.Transformers;

/**
 * The element for a component such as visual component (Swing, AWT or SWT) or
 * predefined ones.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public abstract class ComponentElement<T> extends DecoratedElement {

  public ComponentElement(Element decorated) {
    super(decorated);
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
  public ElementValueHandler in(Object target) {
    return new AbstractElementValueHandler(element, target) {

      @Override
      public void value(Object value) throws HandlingException {
        T component = (T) element.in(target()).value();
        setComponentValue(component, transformForComponent(value));
      }

      @Override
      public <E> E value() throws HandlingException {
        T component = (T) element.in(target()).value();
        return (E) transformForObject(getComponentValue(component));
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
    Formatter formatter = Formatters.factory().create(createContext());
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
    Formatter formatter = Formatters.factory().create(createContext());
    return formatter.parse(value);
  }

  protected Object transformForComponent(Object value) {
    BidirectionalTransformer transformer = Transformers.factory().create(createContext());
    return transformer.inverse(value);
  }

  protected Object transformForObject(Object value) {
    BidirectionalTransformer transformer = Transformers.factory().create(createContext());
    return transformer.transform(value);
  }

  protected AnnotationFactoryContextImpl createContext() {
    return new AnnotationFactoryContextImpl(element, element.target());
  }

  protected abstract Object getComponentValue(T component);

  protected abstract void setComponentValue(T component, Object value);

}
