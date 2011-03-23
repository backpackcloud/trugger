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

import net.sf.trugger.message.MessageCreator;
import net.sf.trugger.validation.ValidationEngine;
import net.sf.trugger.validation.ValidationFactory;
import net.sf.trugger.validation.ValidatorBinder;
import net.sf.trugger.validation.ValidatorFactory;

/**
 * Default factory for validations.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerValidationFactory implements ValidationFactory {

  public ValidatorBinder createValidatorBinder() {
    return new TruggerValidatorBinder();
  }

  public ValidationEngine createValidationEngine(ValidatorFactory validatorFactory, MessageCreator messageCreator) {
    return new TruggerValidationEngine(validatorFactory, messageCreator);
  }

  public ValidatorFactory createValidatorFactory(ValidatorBinder validatorBinder) {
    return new TruggerValidatorFactory(validatorBinder);
  }

}
