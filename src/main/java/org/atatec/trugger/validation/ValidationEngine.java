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
package org.atatec.trugger.validation;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.message.MessageCreator;

import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Interface that defines a class capable of validate an object based on the defined
 * constraints in its elements.
 *
 * @author Marcelo Guimarães
 */
public interface ValidationEngine {

  /**
   * Indicates that the validation must be processed only over the annotations with
   * property <code>context</code> equals to given context.
   *
   * @param context the context validation.
   *
   * @return a reference to this object.
   */
  ValidationEngine forContext(String context);

  /**
   * Sets a message creator for creating the messages.
   *
   * @param messageCreator the message creator to use
   */
  ValidationEngine using(MessageCreator messageCreator);

  /**
   * Sets the resource bundle to search for messages. The {@link
   * org.atatec.trugger.message.Messages#createMessageCreator(java.util.ResourceBundle)
   * default message creator} will be used.
   *
   * @param resourceBundle the resource bundle to use.
   */
  ValidationEngine using(ResourceBundle resourceBundle);

  /**
   * Validates all elements in the given target.
   *
   * @return the validation result
   */
  ValidationResult validate(Object target);

  /**
   * Validates the given {@link org.atatec.trugger.element.Element#isSpecific() specific}
   * element
   *
   * @return the validation result
   */
  ValidationResult validate(Element element);

  /**
   * Validates the given {@link org.atatec.trugger.element.Element#isSpecific() specific}
   * elements
   *
   * @return the validation result
   */
  ValidationResult validate(Collection<Element> elements);

}
