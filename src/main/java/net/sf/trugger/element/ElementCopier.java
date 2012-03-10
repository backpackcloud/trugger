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
package net.sf.trugger.element;

import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.transformer.Transformer;

/**
 * Interface that defines a class that copies elements from a target to another.
 * <p>
 * The objects could be of the same or different types. If they are different,
 * only the elements with the same name and compatible types will be copied.
 * <p>
 * Only the {@link Element#isReadable() readable} elements are copied and only
 * the {@link Element#isWritable() writable} elements receives the value.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface ElementCopier {

  /**
   * Copies only the elements that matches with the given predicate.
   *
   * @return a reference to this object.
   */
  ElementCopier elementsMatching(Predicate<ElementCopy> predicate);

  /**
   * Executes the operation transforming the filtered elements using the given
   * transformer.
   *
   * @param transformer
   *          the transformer to use.
   * @return a reference to this object.
   */
  ElementCopier transformingWith(Transformer<?, ElementCopy> transformer);

  /**
   * Copies only the elements that are not <code>null</code>.
   *
   * @return a reference to this object.
   */
  ElementCopier notNull();

  /**
   * Copies only the elements in the given result.
   * <p>
   * There is no need to specify {@link ElementsSelector#readable() readable}
   * elements.
   *
   * @param selector
   *          the selector for limiting the elements to copy.
   * @return a reference to this object.
   */
  ElementCopier inSelection(ElementsSelector selector);

  /**
   * Sets the source object and copies the elements.
   *
   * @param src
   *          the source object.
   */
  void from(Object src);

}
