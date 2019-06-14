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

/**
 * Interface that defines a class that copies elements from a target to another.
 * <p>
 * The objects could be of the same or different types. If they are different,
 * only the elements with the same name and compatible types will be copied.
 * <p>
 * Only the {@link Element#isReadable() readable} elements are copied and only
 * the {@link Element#isWritable() writable} elements receives the value.
 *
 * @author Marcelo "Ataxexe" Guimarães
 * @since 1.2
 */
public interface ElementCopier {

  /**
   * Sets the source object and copies the elements.
   *
   * @param src
   *          the source object.
   */
  CopyDestinationMapper from(Object src);

}
