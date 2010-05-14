/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation.adapter;

import net.sf.trugger.CreateException;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.reflection.ReflectionException;
import net.sf.trugger.validation.ValidationEngine;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;

/**
 * This factory provides an adapter for using the annotations for Hibernate
 * Validator with the default implementation for the {@link ValidationEngine}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class HibernateValidatorFactory implements ValidatorFactory {

  public boolean canCreate(ValidatorContext key) {
    return key.annotation().annotationType().isAnnotationPresent(org.hibernate.validator.ValidatorClass.class);
  }

  public Validator create(ValidatorContext key) throws CreateException {
    Class<? extends org.hibernate.validator.Validator> validatorClass =
        key.annotation().annotationType().getAnnotation(org.hibernate.validator.ValidatorClass.class).value();
    try {
      return new HibernateValidatorAdapter(Reflection.newInstanceOf(validatorClass));
    } catch (ReflectionException e) {
      throw new CreateException(e);
    }
  }

}
