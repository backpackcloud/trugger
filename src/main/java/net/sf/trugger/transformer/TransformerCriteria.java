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
package net.sf.trugger.transformer;

import net.sf.trugger.predicate.Criteria;

/**
 * Interface that defines a criteria for using a mapped transformer.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.6
 */
public interface TransformerCriteria<E> {

  /**
   * Uses the transformer if the value matches a condition.
   *
   * @return a component for defining the condition.
   */
  <V> Criteria<E, V> when(V value);

  /**
   * Uses the transformer if the value don't match a condition.
   *
   * @return a component for defining the condition.
   */
  <V> Criteria<E, V> whenNot(V value);

  /**
   * Uses the transformer if the value is <code>true</code>.
   */
  void when(Boolean value);

  /**
   * Uses the transformer if the value is <code>true</code>.
   */
  void when(boolean value);

  /**
   * Uses the transformer if the value is <code>false</code>.
   */
  void whenNot(Boolean value);

  /**
   * Uses the transformer if the value is <code>false</code>.
   */
  void whenNot(boolean value);

}
