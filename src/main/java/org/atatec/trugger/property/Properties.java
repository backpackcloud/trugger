/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.property;

import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;

/**
 * An utility class for handling object properties.
 *
 * @author Marcelo Guimarães
 */
public class Properties {

  private static final PropertyFactory factory;

  static {
    factory = ImplementationLoader.get(PropertyFactory.class);
  }

  private Properties() {}

  /**
   * Finds a property in a target.
   *
   * @return the component for find properties.
   */
  public static ElementSelector property(String name) {
    return factory.createPropertySelector(name);
  }

  /**
   * Finds properties in a target.
   *
   * @return the component for find properties.
   */
  public static ElementsSelector properties() {
    return factory.createPropertiesSelector();
  }

}
