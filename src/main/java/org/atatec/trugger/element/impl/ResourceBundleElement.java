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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.UnwritableElementException;

import java.util.ResourceBundle;

/**
 * A class that represents a ResourceBundle element. It uses the mapped keys
 * as the elements.
 *
 * @author Marcelo Guimarães
 */
public final class ResourceBundleElement extends AbstractElement implements Element {

  public ResourceBundleElement(String name) {
    super(name);
  }

  @Override
  public ValueHandler in(Object target) {
    if (target instanceof ResourceBundle) {
      final ResourceBundle bundle = (ResourceBundle) target;
      return new ValueHandler() {

        @Override
        public void set(Object value) throws HandlingException {
          throw new UnwritableElementException("Cannot change a ResourceBundle property.");
        }

        @Override
        public <E> E get() throws HandlingException {
          if (bundle.containsKey(name)) {
            return (E) bundle.getObject(name);
          }
          throw new HandlingException("Key '" + name + "' not defined in target.");
        }
      };
    }
    throw new IllegalArgumentException("Target is not a " + ResourceBundle.class);
  }

  @Override
  public Class<?> declaringClass() {
    return ResourceBundle.class;
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

}
