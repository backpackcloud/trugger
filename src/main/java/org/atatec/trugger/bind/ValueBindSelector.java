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
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface ValueBindSelector {

  Binder to(FieldSelector selector);

  Binder to(FieldsSelector selector);

  Binder to(ElementSelector selector);

  Binder to(ElementsSelector selector);

}
