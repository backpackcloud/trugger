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

import java.util.List;

/**
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class ListElement extends AbstractElement implements Element {

  private int index;

  private List list;

  public ListElement(List list, int index) {
    super(String.valueOf(index));
    this.list = list;
    this.index = index;
  }

  @Override
  public <E> E target() {
    return (E) list;
  }

  @Override
  public boolean isSpecific() {
    return true;
  }

  @Override
  public Class declaringClass() {
    return List.class;
  }

  @Override
  public Class<?> type() {
    return Object.class;
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
  public ValueHandler in(final Object list) {
    return new ValueHandler() {
      @Override
      public <E> E value() throws HandlingException {
        try {
          return (E) ((List) list).get(index);
        } catch (Exception e) {
          throw new HandlingException(e);
        }
      }

      @Override
      public void set(Object value) throws HandlingException {
        try {
          ((List) list).set(index, value);
        } catch (Exception e) {
          throw new HandlingException(e);
        }
      }
    };
  }

}
