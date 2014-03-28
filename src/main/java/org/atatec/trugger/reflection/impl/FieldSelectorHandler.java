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

package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.reflection.FieldHandler;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.selector.FieldSelector;

import java.lang.reflect.Field;

public class FieldSelectorHandler implements FieldHandler {

  private final FieldSelector selector;
  private final Object target;

  public FieldSelectorHandler(FieldSelector selector) {
    this.selector = selector;
    this.target = null;
  }

  public FieldSelectorHandler(FieldSelector selector, Object target) {
    this.selector = selector;
    this.target = target;
  }

  @Override
  public <E> E get() throws HandlingException {
    Field field = selector.in(target);
    return Reflection.handle(field).in(target).get();
  }

  @Override
  public void set(Object value) throws HandlingException {
    Field field = selector.in(target);
    Reflection.handle(field).in(target).set(value);
  }

  @Override
  public ValueHandler in(Object source) {
    return new FieldSelectorHandler(selector, source);
  }

}
