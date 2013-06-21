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

import org.atatec.trugger.bind.Binder;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorBinder;
import org.atatec.trugger.validation.ValidatorContext;

import java.util.LinkedList;
import java.util.List;

/** @author Marcelo Guimarães */
public class CompositeValidatorBinder implements ValidatorBinder {

  private final List<ValidatorBinder> binders;

  public CompositeValidatorBinder() {
    this(new LinkedList<ValidatorBinder>());
  }

  public CompositeValidatorBinder(List<ValidatorBinder> binders) {
    this.binders = binders;
  }

  public void add(ValidatorBinder binder) {
    this.binders.add(binder);
  }

  public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
    for (ValidatorBinder validatorBinder : binders) {
      validatorBinder.configureBinds(validator, context, binder);
    }
  }

}
