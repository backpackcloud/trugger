/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;

import java.lang.reflect.Array;

/** @author Marcelo Guimarães */
public class ArrayElement extends AbstractElement implements Element {

  private int index;

  private Class type;

  private Class arrayClass;

  private Object array;

  public ArrayElement(Object array, int index) {
    super(String.valueOf(index));
    this.array = array;
    this.index = index;
    this.type = array.getClass();
    this.arrayClass = type.getComponentType();
  }

  @Override
  public <E> E target() {
    return (E) array;
  }

  @Override
  public boolean isSpecific() {
    return true;
  }

  @Override
  public Class declaringClass() {
    return type;
  }

  @Override
  public Class<?> type() {
    return arrayClass;
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return true;
  }

  @Override
  public ValueHandler on(final Object array) {
    return new ValueHandler() {
      @Override
      public <E> E get() throws HandlingException {
        return (E) Array.get(array, index);
      }

      @Override
      public void set(Object value) throws HandlingException {
        Array.set(array, index, value);
      }
    };
  }

}
