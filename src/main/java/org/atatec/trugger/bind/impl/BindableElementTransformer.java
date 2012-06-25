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
package org.atatec.trugger.bind.impl;

import java.lang.reflect.Field;

import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.FieldElement;
import org.atatec.trugger.element.impl.TruggerBindableElement;
import org.atatec.trugger.transformer.Transformer;

/**
 * A transformer that allows conversion of {@link Element} and {@link Field}
 * objects into a {@link BindableElement} object.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class BindableElementTransformer implements Transformer<BindableElement, Object> {

  private final Object target;

  /**
   * Craetes a new transformer using the give target as the one that will
   * receive the values.
   */
  public BindableElementTransformer(Object target) {
    this.target = target;
  }

  public BindableElement transform(Object object) {
    if (object instanceof BindableElement) {
      return (BindableElement) object;
    } else if (object instanceof Element) {
      return new TruggerBindableElement((Element) object, target);
    } else if (object instanceof Field) {
      return new TruggerBindableElement(new FieldElement((Field) object), target);
    }
    return null;
  }
}
