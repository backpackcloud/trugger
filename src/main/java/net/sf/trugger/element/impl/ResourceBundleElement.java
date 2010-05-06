/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element.impl;

import java.util.ResourceBundle;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.UnwritableElementException;

/**
 * A class that represents a ResourceBundle element. It uses the mapped keys
 * as the elements.
 *
 * @author Marcelo Varella Barca Guimarães
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
        public void value(Object value) throws HandlingException {
          throw new UnwritableElementException("Cannot change a ResourceBundle property.");
        }
        
        @Override
        public <E> E value() throws HandlingException {
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
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ResourceBundleElement other = (ResourceBundleElement) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }
  
}
