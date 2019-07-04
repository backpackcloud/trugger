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
package io.backpackcloud.trugger.element;

import java.util.List;
import java.util.Optional;

/**
 * Interface that defines a class capable of search a source for elements
 *
 * @author Marcelo Guimaraes
 * @since 6.0
 */
public interface ElementFinder {

  /**
   * Checks if this finder can search for elements of the given type.
   *
   * @param type the type to test
   * @return {@code true} if this finder can be used against the given type
   */
  boolean canFind(Class type);

  /**
   * Finds the element with the specified name on the given target.
   *
   * @param name   the object name to find.
   * @param target the target to search
   * @return the found object
   */
  Optional<Element> find(String name, Object target);

  /**
   * Finds all objects on the given target.
   *
   * @param target the target to search
   * @return the found objects
   */
  List<Element> findAll(Object target);

}
