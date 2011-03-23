/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation.impl;

import net.sf.trugger.Resolver;
import net.sf.trugger.element.Element;
import net.sf.trugger.validation.ValidationBridge;
import net.sf.trugger.validation.Validator;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ValidatorReferenceResolver implements Resolver<Object, Element> {
  
  private final ValidationBridge bridge;
  
  public ValidatorReferenceResolver(ValidationBridge bridge) {
    this.bridge = bridge;
  }
  
  public Validator resolve(Element target) {
    return bridge.createValidator(target);
  }
  
}
