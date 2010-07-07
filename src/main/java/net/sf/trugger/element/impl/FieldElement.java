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

import java.lang.reflect.Field;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.NonSpecificElementException;
import net.sf.trugger.reflection.Reflection;

/**
 * An {@link Element} originated from a {@link Field}.
 *
 * @author Marcelo Varella Barca Guimarães
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((field == null) ? 0 : field.hashCode());
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
    FieldElement other = (FieldElement) obj;
    if (field == null) {
      if (other.field != null) {
        return false;
      }
    } else if (!field.equals(other.field)) {
      return false;
    }
    return true;
  }

}
