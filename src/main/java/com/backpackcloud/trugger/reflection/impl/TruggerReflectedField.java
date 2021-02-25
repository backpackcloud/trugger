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

package com.backpackcloud.trugger.reflection.impl;

import com.backpackcloud.trugger.reflection.ReflectedField;
import com.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * A class that holds a reflected field
 *
 * @since 7.0
 */
public class TruggerReflectedField extends TruggerReflectedObject<Field> implements ReflectedField {

  private final Field field;
  private final Object target;

  /**
   * Creates a new instance of this class
   *
   * @param field  the reflected field
   * @param target the target from which the field was reflected
   */
  public TruggerReflectedField(Field field, Object target) {
    super(field);
    this.field = field;
    this.target = target;
  }

  @Override
  public <E> E getValue() {
    return Reflection.handle(field).on(target).getValue();
  }

  @Override
  public void setValue(Object newValue) {
    Reflection.handle(field).on(target).setValue(newValue);
  }

  @Override
  public Field unwrap() {
    return this.field;
  }

  @Override
  public Object target() {
    return target;
  }

  // Delegated methods

  @Override
  public boolean isEnumConstant() {
    return field.isEnumConstant();
  }

  @Override
  public Class<?> getType() {
    return field.getType();
  }

  @Override
  public Type getGenericType() {
    return field.getGenericType();
  }

}
