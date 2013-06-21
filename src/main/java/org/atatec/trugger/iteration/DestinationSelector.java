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

package org.atatec.trugger.iteration;

import java.util.Collection;

/**
 * Interface for defining the destination collection parameter.
 *
 * @author Marcelo Guimarães
 */
public interface DestinationSelector {

  /**
   * Defines the destination collection and executes the operation.
   *
   * @param collection the destination collection.
   */
  void to(Collection collection);

}
