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
package com.backpackcloud.trugger.reflection;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for a single {@link Field} object assuming
 * that the name was specified before.
 *
 * @author Marcelo Guimaraes
 */
public interface FieldSelector {

  /**
   * Selects the elements that matches with the given predicate.
   *
   * @param predicate
   *          the predicate to match.
   * @return a new selector with the given filter
   */
  FieldSelector filter(Predicate<? super Field> predicate);

  /**
   * Selects using a deep operation through the target's inheritance. If
   * the selection is for a single object and the deep operation founds
   * more than one, the first one will be returned.
   *
   * @return a new selector with deep selection enabled
   */
  FieldSelector deep();

  /**
   * Applies the selection on the given target
   *
   * @param target the target to apply the selection
   * @return the result
   */
  Optional<ReflectedField> from(Object target);

}
