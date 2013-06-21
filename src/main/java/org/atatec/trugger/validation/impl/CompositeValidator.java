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

import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Marcelo Guimarães
 */
final class CompositeValidator implements Validator {
  
  private final Collection<ValidatorInvoker> validators;
  
  public CompositeValidator() {
    this.validators = new ArrayList<ValidatorInvoker>();
  }
  
  public void add(ValidatorInvoker validator) {
    validators.add(validator);
  }
  
  public boolean isValid(Object value) {
    for (ValidatorInvoker validator : validators) {
      if(!validator.isValid(value)) {
        return false;
      }
    }
    return true;
  }
  
}
