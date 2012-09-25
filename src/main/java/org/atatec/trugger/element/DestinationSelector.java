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

package org.atatec.trugger.element;

import org.atatec.trugger.transformer.Transformer;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public interface DestinationSelector {

  /**
   * Executes the operation transforming the elements with the given transformer.
   *
   * @param transformer the transformer to use.
   */
  DestinationSelector as(Transformer<?, ElementCopy> transformer);

  /** Copies only the elements that are not <code>null</code>. */
  DestinationSelector notNull();

  /**
   * Copy the elements to the given object.
   *
   * @param dest the object to copy the elements.
   */
  void to(Object dest);

}
