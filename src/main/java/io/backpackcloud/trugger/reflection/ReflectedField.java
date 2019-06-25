/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package io.backpackcloud.trugger.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * A class that holds a reflected field
 *
 * @since 7.0
 */
public class ReflectedField extends ReflectedObject {

  private final Field field;
  private final Object target;

  /**
   * Creates a new instance of this class
   *
   * @param field  the reflected field
   * @param target the target from which the field was reflected
   */
  public ReflectedField(Field field, Object target) {
    super(field);
    this.field = field;
    this.target = target;
  }

  /**
   * Gets the value of the field on the target instance
   *
   * @return the value of the field on the target instance
   */
  public <E> E getValue() {
    return Reflection.handle(field).on(target).getValue();
  }

  /**
   * Sets this field value
   *
   * @param newValue
   */
  public void setValue(Object newValue) {
    Reflection.handle(field).on(target).setValue(newValue);
  }

  /**
   * Returns the enclosed field
   *
   * @return the enclosed field
   */
  public Field actualField() {
    return this.field;
  }

  /**
   * Returns the target in which this field was reflected from
   *
   * @return the target in which this field was reflected from
   */
  public Object target() {
    return target;
  }

  // Delegated methods

  /**
   * @see Field#isEnumConstant()
   */
  public boolean isEnumConstant() {
    return field.isEnumConstant();
  }

  /**
   * @see Field#getType()
   */
  public Class<?> getType() {
    return field.getType();
  }

  /**
   * @see Field#getGenericType()
   */
  public Type getGenericType() {
    return field.getGenericType();
  }

}
