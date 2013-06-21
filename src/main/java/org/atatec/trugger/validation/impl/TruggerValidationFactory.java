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
package org.atatec.trugger.validation.impl;

import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.ValidationFactory;
import org.atatec.trugger.validation.ValidatorBinder;
import org.atatec.trugger.validation.ValidatorFactory;

/**
 * Default factory for validations.
 *
 * @author Marcelo Guimarães
 */
public class TruggerValidationFactory implements ValidationFactory {

  public ValidatorBinder createValidatorBinder() {
    return new TruggerValidatorBinder();
  }

  public ValidatorFactory createValidatorFactory(ValidatorBinder binder) {
    return new TruggerValidatorFactory(binder);
  }

  public ValidationEngine createValidationEngine(ValidatorFactory validatorFactory) {
    return new TruggerValidationEngine(validatorFactory);
  }

}
