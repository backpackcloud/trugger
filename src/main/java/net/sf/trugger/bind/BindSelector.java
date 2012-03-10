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
package net.sf.trugger.bind;

import java.lang.reflect.Field;

import net.sf.trugger.selector.ElementSpecifier;
import net.sf.trugger.selector.FieldSpecifier;

/**
 * Interface for the bind configuration for a {@link Binder#bind(Object) value}
 * or {@link Binder#use(net.sf.trugger.Resolver) resolver}.
 * <p>
 * There are some conventions for keeping compatibility against the binds:
 * <ul>
 * <li>The bind using a selection of {@link Field} objects must use the target
 * hierarchy.
 * </ul>
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface BindSelector {

  /**
   * Binds to a field selection.
   * <p>
   * This bind applies only to non-final fields and uses the target hierarchy.
   *
   * @param fieldName
   *          the field name.
   * @return a component for make the selection.
   */
  FieldSpecifier toField(String fieldName);

  /**
   * Binds to a selection of fields.
   * <p>
   * This bind applies only to non-final fields and uses the target hierarchy.
   *
   * @return a component for make the selection.
   */
  FieldSpecifier toFields();

  /**
   * Binds to a field selection.
   * <p>
   * This bind applies only to non-final fields and uses the target hierarchy.
   *
   * @return a component for make the selection.
   * @since 2.7
   */
  FieldSpecifier toField();

  /**
   * Binds to a selection of bindable elements.
   *
   * @return a component for make the selection.
   */
  ElementSpecifier toElements();

  /**
   * Binds to a bindable element selection.
   *
   * @param name
   *          the element name.
   * @return a component for make the selection.
   */
  ElementSpecifier toElement(String name);

  /**
   * Binds to a bindable element selection.
   *
   * @return a component for make the selection.
   * @since 2.7
   */
  ElementSpecifier toElement();

}
