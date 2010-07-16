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

import java.lang.reflect.AnnotatedElement;

import net.sf.trugger.HandlingException;
import net.sf.trugger.element.ElementValueHandler;
import net.sf.trugger.factory.AnnotationFactoryContext;
import net.sf.trugger.factory.AnnotationFactoryContextImpl;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.format.Formatters;

/**
 * Base class for ElementValueHandler.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public abstract class AbstractElementValueHandler implements ElementValueHandler {

  private final AnnotatedElement annotatedElement;
  private final Object target;

  public AbstractElementValueHandler(AnnotatedElement annotatedElement, Object target) {
    this.annotatedElement = annotatedElement;
    this.target = target;
  }

  @Override
  public String formattedValue() throws HandlingException {
    return format(value());
  }

  @Override
  public void formattedValue(String value) throws HandlingException {
    value(parse(value));
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

  private AnnotationFactoryContext createContext() {
    AnnotationFactoryContextImpl context = new AnnotationFactoryContextImpl();
    context.setAnnotatedElement(annotatedElement);
    context.setTarget(target());
    return context;
  }

  protected Object target() {
    return target;
  }

}
