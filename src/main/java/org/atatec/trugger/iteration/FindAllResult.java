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

package org.atatec.trugger.iteration;

import java.util.Collection;
import java.util.List;

/**
 * Interface for defining the source collection in a "find all" operation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public interface FindAllResult {

  /**
   * Defines the source collection and executes the operation.
   *
   * @param collection the source collection
   *
   * @return the operation result
   */
  <E> List<E> in(Collection<E> collection);

}
