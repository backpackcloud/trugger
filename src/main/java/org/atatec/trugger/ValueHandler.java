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
package org.atatec.trugger;

/**
 * Interface that defines a component for handling, modifying and/or accessing a
 * value according to the implementation behavior.
 * 
 * @author Marcelo Guimarães
 */
public interface ValueHandler {
  
  /**
   * Tries to get the value.
   * 
   * @return the value.
   * @throws HandlingException
   *           if anything go wrong.
   */
  <E> E value() throws HandlingException;
  
  /**
   * Tries to set the value.
   * 
   * @param value
   *          the value to set.
   * @throws HandlingException
   *           if anything go wrong
   */
  void set(Object value) throws HandlingException;
  
}
