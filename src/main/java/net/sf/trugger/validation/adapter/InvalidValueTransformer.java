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
package net.sf.trugger.validation.adapter;

import net.sf.trugger.Transformer;
import net.sf.trugger.message.MessagePart;
import net.sf.trugger.validation.InvalidElement;

import org.hibernate.validator.InvalidValue;

/**
 * A transformer for converting an {@link InvalidElement} into an
 * {@link InvalidValue}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.6
 */
public class InvalidValueTransformer implements Transformer<InvalidValue, InvalidElement> {

  @Override
  public InvalidValue transform(InvalidElement el) {
    return new InvalidValue(convertMessage(el), el.declaringClass(), el.name(), el.value(), el.target());
  }

  /**
   * Converts the message.
   *
   * @param el
   *          the invalid element
   * @return the converted message.
   */
  protected String convertMessage(InvalidElement el) {
    return el.joinMessages("; ", MessagePart.SUMMARY);
  }

}
