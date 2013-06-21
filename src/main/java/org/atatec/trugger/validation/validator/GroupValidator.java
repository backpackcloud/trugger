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
package org.atatec.trugger.validation.validator;

import org.atatec.trugger.annotation.TargetAnnotation;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.annotation.TargetObject;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.*;
import org.atatec.trugger.validation.impl.ValidatorContextImpl;

import java.lang.annotation.Annotation;

/**
 * This is a common validator for grouping validations in a single annotation.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
public class GroupValidator implements Validator<Object> {

  @TargetAnnotation
  private Annotation annotation;
  @TargetObject
  private Object target;
  @TargetElement
  private Element element;
  private ValidationBridge bridge;

  public boolean isValid(Object value) {
    if (bridge != null) {
      return bridge.validate(annotation.annotationType(), value);
    }
    ValidatorFactory factory = Validation.newValidatorFactory();
    Annotation[] declaredAnnotations = annotation.annotationType().getDeclaredAnnotations();
    ValidatorContext context;
    for (Annotation an : declaredAnnotations) {
      context = new ValidatorContextImpl()
        .annotation(an)
        .element(element)
        .target(target);
      if (factory.canCreate(context)) {
        ValidatorInvoker validator = factory.create(context);
        if (!validator.isValid(value)) {
          return false;
        }
      }
    }
    return true;
  }

}
