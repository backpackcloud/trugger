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
package net.sf.trugger.util.mock;

/**
 * Interface inherited by all mock builders.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The mock object class.
 */
public interface MockBuilder<T> {
  
  /**
   * Activate and return the mock object.
   * 
   * @return the activated mock object.
   */
  T mock();
  
}
