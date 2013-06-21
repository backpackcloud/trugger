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

import org.atatec.trugger.CreateException;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.util.LinkedList;
import java.util.List;

/** @author Marcelo Guimarães */
public final class CompositeValidatorFactory implements ValidatorFactory {

  private final List<ValidatorFactory> factories;

  public CompositeValidatorFactory() {
    this(new LinkedList<ValidatorFactory>());
  }

  public CompositeValidatorFactory(List<ValidatorFactory> factories) {
    this.factories = factories;
  }

  public void add(ValidatorFactory factory) {
    factories.add(factory);
  }

  public boolean canCreate(ValidatorContext key) {
    for (ValidatorFactory vf : factories) {
      if (vf.canCreate(key)) {
        return true;
      }
    }
    return false;
  }

  public ValidatorInvoker create(ValidatorContext key) throws CreateException {
    for (ValidatorFactory vf : factories) {
      if (vf.canCreate(key)) {
        return vf.create(key);
      }
    }
    throw new CreateException("Cannot create a validator for " + key.annotation().annotationType().getName());
  }
}
