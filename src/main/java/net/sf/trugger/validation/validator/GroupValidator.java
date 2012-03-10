/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation.validator;

import net.sf.trugger.annotation.TargetAnnotation;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.annotation.TargetObject;
import net.sf.trugger.element.Element;
import net.sf.trugger.validation.*;
import net.sf.trugger.validation.impl.ValidatorContextImpl;

import java.lang.annotation.Annotation;

/**
 * This is a common validator for grouping validations in a single annotation.
 *
 * @author Marcelo Varella Barca Guimarães
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
      context = new ValidatorContextImpl(an, element, target);
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
