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

import java.lang.reflect.AnnotatedElement;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class DefaultElementValueHandler extends AbstractElementValueHandler {

  private final ValueHandler valueHandler;

  public DefaultElementValueHandler(AnnotatedElement annotatedElement, ValueHandler valueHandler, Object target) {
    super(annotatedElement, target);
    this.valueHandler = valueHandler;
  }

  @Override
  public <E> E value() throws HandlingException {
    return (E) valueHandler.value();
  }

  @Override
  public void value(Object value) throws HandlingException {
    valueHandler.value(value);
  }

}
