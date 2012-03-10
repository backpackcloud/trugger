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

import net.sf.trugger.element.Element;

/**
 * A common interface for elements that can have their values binded.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface BindableElement extends Element {

  /**
   * Binds the given value to this element.
   * <p>
   * This has the same effect as method {@link #value(Object)}.
   *
   * @param value
   *          the value to bind.
   */
  void bind(Object value);

  /**
   * Should always return <code>true</code>.
   */
  boolean isSpecific();

  /**
   * Returns the target which the value can be binded.
   */
  <E> E target();

}
