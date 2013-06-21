/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.bind;

import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.selector.FieldsSelector;

import java.lang.reflect.Field;

/**
 * Interface for the bind configuration for a {@link Binder#bind(Object) value}.
 * <p/>
 * There are some conventions for keeping compatibility against the binds: <ul> <li>The
 * bind using a selection of {@link Field} objects must use the target hierarchy. </ul>
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public interface ValueBindSelector {

  /** Binds the value to the field returned by the given selector. */
  Binder to(FieldSelector selector);

  /** Binds the value to the fields returned by the given selector. */
  Binder to(FieldsSelector selector);

  /** Binds the value to the element returned by the given selector. */
  Binder to(ElementSelector selector);

  /** Binds the value to the elements returned by the given selector. */
  Binder to(ElementsSelector selector);

}
