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

package org.atatec.trugger.validation;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.message.MessageCreator;

import java.util.Collection;
import java.util.ResourceBundle;

/** @author Marcelo Varella Barca Guimarães */
public interface ContextValidationEngine {

  ContextValidationEngine using(MessageCreator messageCreator);

  ContextValidationEngine using(ResourceBundle resourceBundle);

  ValidationResult validate(Object target);

  ValidationResult validate(Element element);

  ValidationResult validate(Collection<Element> elements);

}
