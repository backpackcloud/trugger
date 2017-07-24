/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.reflection;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;

import java.lang.reflect.Field;

/**
 * A handler for {@link Field} objects.
 * <p>
 * For handling a static field, you don't need to specify that instance:
 * <p>
 * <pre>
 * FieldHandler handler = {@link Reflection#handle(Field)};
 * Object oldValue = handler.value();
 * handler.value(newValue);
 * </pre>
 * <p>
 * For handling a non-static field, you must specify an instance:
 * <p>
 * <pre>
 * FieldHandler handler = {@link Reflection#handle(Field)};
 * Object oldValue = handler.on(instance).get();
 * handler.on(instance).value(newValue);
 * </pre>
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public interface FieldHandler extends ValueHandler {

  /**
   * Gets the value for a <i>static</i> {@link Field}.
   */
  <E> E getValue() throws HandlingException;

  /**
   * Sets the value for a <i>static</i> {@link Field}.
   */
  void setValue(Object value) throws HandlingException;

  /**
   * Handles the value on a given instance (use this if the field is not
   * <i>static</i>).
   *
   * @param source the instance. A <code>null</code> value or a {@link Class} may be
   *               passed if the field is static.
   * @return the component for handling the field on the given instance.
   */
  ValueHandler on(Object source);

}
