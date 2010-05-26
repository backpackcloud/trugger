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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.trugger.Transformer;
import net.sf.trugger.element.Elements;
import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.validation.InvalidElement;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationResult;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;


/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class ClassValidatorTransformer implements Transformer<ClassValidator, Validation> {

  private final Transformer<InvalidValue, InvalidElement> transformer;

  public ClassValidatorTransformer() {
    this(new InvalidValueTransformer());
  }

  public ClassValidatorTransformer(Transformer<InvalidValue, InvalidElement> invalidValueTransformer) {
    this.transformer = invalidValueTransformer;
  }

  @Override
  public ClassValidator transform(final Validation validation) {
    return new ClassValidator(null) {

      private InvalidValue[] getInvalids(ValidationResult result) {
        Collection<InvalidElement> invalidElements = result.invalidElements();
        List<InvalidValue> invalids = new ArrayList<InvalidValue>(invalidElements.size());
        Iteration.copyTo(invalids).transformingWith(transformer).allElements().from(invalidElements);
        return invalids.toArray(new InvalidValue[invalids.size()]);
      }

      public void assertValid(Object bean) {
        ValidationResult result = validation.validate().allElements().in(bean);
        if(result.isInvalid()) {
          throw new InvalidStateException(getInvalids(result));
        }
      }
      public InvalidValue[] getInvalidValues(Object bean, String name) {
        return getInvalids(validation.validate().selection(Elements.element(name)).in(bean));
      }
      public InvalidValue[] getInvalidValues(Object bean) {
        return getInvalids(validation.validate().allElements().in(bean));
      }
      public InvalidValue[] getPotentialInvalidValues(String name, Object bean) {
        return getInvalids(validation.validate().selection(Elements.element(name)).in(bean));
      }
    };
  }

}
