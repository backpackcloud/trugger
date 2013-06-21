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
import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.bind.Bind;
import org.atatec.trugger.bind.Binder;
import org.atatec.trugger.factory.AnnotationBasedFactory;
import org.atatec.trugger.validation.InvalidReferenceException;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorBinder;
import org.atatec.trugger.validation.ValidatorClass;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.lang.annotation.Annotation;

/**
 * A default implementation for the validator factory.
 * <p/>
 * If the validation must use a reference value present in the target object. Just
 * annotate a <i>String</i> property in the annotation that indicates the element that
 * must be binded with the {@link Reference} annotation. The reference can also have their
 * specific validation in the property of the annotation.
 *
 * @author Marcelo Guimarães
 */
public class TruggerValidatorFactory implements ValidatorFactory {

  private static final Validator NULL_VALIDATOR = new Validator() {

    public boolean isValid(Object value) {
      return true;
    }
  };

  private final AnnotationBasedFactory<ValidatorClass, Validator> factory =
    new AnnotationBasedFactory<ValidatorClass, Validator>(ValidatorClass.class);

  private ValidatorBinder validatorBinder;

  public TruggerValidatorFactory(ValidatorBinder validatorBinder) {
    this.validatorBinder = validatorBinder;
  }

  public boolean canCreate(ValidatorContext key) {
    return factory.canCreate(key.annotation().annotationType());
  }

  public ValidatorInvoker create(ValidatorContext key) throws CreateException {
    Annotation annotation = key.annotation();
    Class<? extends Annotation> annotationType = annotation.annotationType();
    Validator validator = factory.create(annotationType);
    Binder binder = Bind.binds();
    try {
      validatorBinder.configureBinds(validator, key, binder);
    } catch (InvalidReferenceException e) {
      return new TruggerValidatorInvoker(NULL_VALIDATOR);
    }
    binder.applyIn(validator);
    return new TruggerValidatorInvoker(validator);
  }

}
