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

import java.util.List;

import net.sf.trugger.CreateException;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public final class CompositeValidatorFactory implements ValidatorFactory {

  private final List<ValidatorFactory> factories;

  public CompositeValidatorFactory(List<ValidatorFactory> factories) {
    this.factories = factories;
  }

  public boolean canCreate(ValidatorContext key) {
    for (ValidatorFactory vf : factories) {
      if (vf.canCreate(key)) {
        return true;
      }
    }
    return false;
  }

  public Validator create(ValidatorContext key) throws CreateException {
    for (ValidatorFactory vf : factories) {
      if (vf.canCreate(key)) {
        return vf.create(key);
      }
    }
    throw new CreateException("Cannot create a validator for " + key.annotation().annotationType().getName());
  }

}
