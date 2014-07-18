/*
 * Copyright 2009-2014 Marcelo Guimarães
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

package tools.devnull.trugger;

/**
 * Interface that maps two things inside a fluent interface.
 *
 * @since 5.0
 */
public interface ObjectMapper<T, R> {

  /**
   * Maps to the given object.
   *
   * @param value the value to map
   * @return a reference to the object for doing other mappings.
   */
  R to(T value);

}
