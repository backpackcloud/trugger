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

import org.atatec.trugger.message.MessageCreator;

import java.util.ResourceBundle;

/**
 * Interface that defines a class capable of validate an object based on the
 * Metadatas of its properties.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface ValidationEngine extends ContextValidationEngine {

  /**
   * Indicates that the validation must be processed only over the annotations
   * with property <code>context</code> equals to given context.
   *
   * @param context
   *          the context validation.
   * @return a reference to this object.
   */
  ContextValidationEngine forContext(String context);

  ValidationEngine using(MessageCreator messageCreator);

  ValidationEngine using(ResourceBundle resourceBundle);

}
