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
package tools.devnull.trugger.element;

/**
 * Interface that defines a copy of an {@link Element}.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public interface ElementCopy {

  /**
   * @return the source element value.
   */
  <E> E value();

  /**
   * @return the source element.
   */
  Element src();

  /**
   * @return the element that may receive the value. Note that this element may
   *         be the {@link #src() source element} if the targets are
   *         from the same class.
   */
  Element dest();

}
