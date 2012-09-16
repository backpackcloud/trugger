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

/**
 * Interface for the bind configuration for a {@link org.atatec.trugger.bind.Binder#use(org.atatec.trugger.Resolver)
 * resolver}.
 * <p/>
 * There are some conventions for keeping compatibility against the binds: <ul> <li>The
 * bind using a selection of {@link java.lang.reflect.Field} objects must use the target
 * hierarchy. </ul>
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface ResolverBindSelector {

  Binder in(FieldSelector selector);

  Binder in(FieldsSelector selector);

  Binder in(ElementSelector selector);

  Binder in(ElementsSelector selector);

}
