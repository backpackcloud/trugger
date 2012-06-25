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
 * Interface that defines a predicate of an element.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The element type
 */
public interface Predicate<T> {
  
  /**
   * Evaluates the element and returns a flag.
   * 
   * @param element
   *          the element to be tested
   * @return a flag based on the given object.
   */
  public boolean evaluate(T element);

}
