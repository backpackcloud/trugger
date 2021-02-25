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

import com.backpackcloud.trugger.HandlingException;
import com.backpackcloud.trugger.ValueHandler;
import com.backpackcloud.trugger.reflection.FieldHandler;
import com.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.Field;

/**
 * Implementation for the FieldHandler interface.
 * 
 * @author Marcelo Guimaraes
 */
public class TruggerFieldHandler implements FieldHandler {
  
  private final Field field;
  
  private final Object instance;
  
  /**
   * Constructs a new handler for a static field.
   * 
   * @param field
   *          the field to be handled
   */
  public TruggerFieldHandler(Field field) {
    if (!field.isAccessible()) {
      Reflection.setAccessible(field);
    }
    this.field = field;
    this.instance = null; //for static access
  }
  
  /**
   * Constructs a new handler for a non-static field.
   * 
   * @param field
   *          the field to be handled
   * @param instance
   *          an instance that declares this field
   */
  private TruggerFieldHandler(Field field, Object instance) {
    super();
    this.field = field;
    this.instance = instance;
  }
  
  public <E> E getValue() throws HandlingException {
    try {
      return (E) field.get(instance);
    } catch (Exception e) {
      throw new HandlingException(e);
    }
  }
  
  public void setValue(Object value) throws HandlingException {
    try {
      field.set(instance, value);
    } catch (Exception e) {
      throw new HandlingException(e);
    }
  }
  
  public ValueHandler on(Object source) {
    return new TruggerFieldHandler(field, source);
  }
  
}
