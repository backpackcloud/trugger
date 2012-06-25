/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.reflection;

import java.lang.reflect.Field;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.Result;
import org.atatec.trugger.ValueHandler;

/**
 * A handler for {@link Field} objects.
 * <p>
 * For handling a static field, you don't need to specify any instance:
 * 
 * <pre>
 * FieldHandler handler = {@link Reflection#handle(Field)};
 * Object oldValue = handler.value();
 * handler.value(newValue);
 * </pre>
 * 
 * For handling a non-static field, you must specify an instance:
 * 
 * <pre>
 * FieldHandler handler = {@link Reflection#handle(Field)};
 * Object oldValue = handler.on(instance).get();
 * handler.on(instance).value(newValue);
 * </pre>
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface FieldHandler extends ValueHandler, Result<ValueHandler, Object> {
  
  /**
   * Gets the value for a <i>static</i> {@link Field}.
   */
  <E> E value() throws HandlingException;
  
  /**
   * Sets the value for a <i>static</i> {@link Field}.
   */
  void value(Object value) throws HandlingException;
  
  /**
   * Handles the value on a given instance (use this if the field is not
   * <i>static</i>).
   * 
   * @param source
   *          the instance. A <code>null</code> value or a {@link Class} may be
   *          passed if the field is static.
   * @return the component for handling the field on the given instance.
   */
  ValueHandler in(Object source);
  
}
