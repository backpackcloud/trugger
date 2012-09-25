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
package org.atatec.trugger.predicate;

/**
 * Interface that defines a predicate that allows composition by a set of common
 * operations based on boolean algebra.
 * <p>
 * <strong>Every instance of this interface must be <i>immutable</i>. The
 * composition methods should always return new instances.</strong>
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The element type.
 */
public interface CompositePredicate<T> extends Predicate<T> {
  
  /**
   * Composes this predicate with the given one using the AND operation.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> and(Predicate predicate);
  
  /**
   * Composes this predicate with the given one using the NAND operation.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> nand(Predicate predicate);
  
  /**
   * Composes this predicate with the given one using the OR operation.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> or(Predicate predicate);
  
  /**
   * Composes this predicate with the given one using the NOR operation.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> nor(Predicate predicate);
  
  /**
   * Composes this predicate with the given one using the XAND operation.
   * <p>
   * Note: The XOR operation returns <code>true</code> only if the predicates
   * return the same result.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> xand(Predicate predicate);
  
  /**
   * Composes this predicate with the given one using the XOR operation.
   * <p>
   * Note: The XOR operation returns <code>true</code> only if the predicates
   * return different results.
   * 
   * @param predicate
   *          the predicate to compose.
   * @return the created predicate.
   */
  CompositePredicate<T> xor(Predicate predicate);
  
  /**
   * Negates this predicate.
   * 
   * @return the created predicate.
   */
  CompositePredicate<T> negate();
  
}
