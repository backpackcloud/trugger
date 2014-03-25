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
import org.atatec.trugger.element.NonSpecificElementException;
import org.atatec.trugger.reflection.Reflection;

import java.lang.reflect.Field;

/**
 * An {@link Element} originated from a {@link Field}.
 *
 * @author Marcelo Guimarães
 */
public final class FieldElement extends AbstractElement implements Element {

  private final Field field;

  /**
   * Creates a new FieldElement based on the given field.
   */
  public FieldElement(Field field) {
    super(field.getName());
    this.field = field;
    this.annotatedElement = field;
  }

  @Override
  public Class<?> declaringClass() {
    return field.getDeclaringClass();
  }

  @Override
  public Class<?> type() {
    return field.getType();
  }

  @Override
  public Object value() throws HandlingException {
    if(isSpecific()) {
      return in(null).value();
    }
    throw new NonSpecificElementException();
  }

  @Override
  public void value(Object value) throws HandlingException {
    if (isSpecific()) {
      in(null).value(value);
    } else {
      throw new NonSpecificElementException();
    }
  }

  public ValueHandler in(Object target) {
    return Reflection.handle(this.field).in(target);
  }

  public boolean isReadable() {
    return true; // the invoker will set the field accessible before accessing it
  }

  public boolean isWritable() {
    return !Reflection.isFinal(field);
  }

  @Override
  public boolean isSpecific() {
    return Reflection.isStatic(field);
  }

}
