/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.property;

import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementsSelector;

/**
 * Interface that defines a factory for creating components for manipulating
 * properties.
 * <p>
 * A property is composed by any combination of these members:
 * <ul>
 * <li>a private field
 * <li>a getter method
 * <li>a setter method
 * </ul>
 * All of this member must refer to same name and type (for example, a String
 * field named "myField" plus a getter named "getMyField" that return a String
 * and a setter named "setMyField" that takes a String).
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface PropertyFactory {
  
  /**
   * Creates the implementation for the property selection operation.
   * 
   * @param name
   *          the property name.
   * @return the component for this operation.
   */
  ElementSelector createPropertySelector(String name);
  
  /**
   * Creates the implementation for the property selection operation.
   * 
   * @return the component for this operation.
   */
  ElementsSelector createPropertiesSelector();
  
}
