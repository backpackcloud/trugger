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
package net.sf.trugger.validation.impl;

import java.lang.annotation.Annotation;
import java.util.Set;

import net.sf.trugger.annotation.Reference;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;

/**
 * @author Marcelo Varella Barca Guimarães
 */
class ValidatorForElement implements Validator {

  public final ValidatorFactory validatorFactory;
  public final Validator validator;
  public final ValidatorContext context;
  public final boolean isReferencesValids;

  ValidatorForElement(ValidatorFactory factory, Validator validator, ValidatorContext context,
      ValidationParameter parameter, Binder binder) {
    this.validator = validator;
    this.validatorFactory = factory;
    this.context = context;
    this.isReferencesValids = checkReferences(parameter, context.annotation(), binder, validator);
    if (isReferencesValids) {
      binder.applyBinds(validator);
    }
  }

  /**
   * Checks the references that the annotation needs. Binding the values in the
   * given binder.
   */
  private boolean checkReferences(ValidationParameter parameter, Annotation annotation, Binder binder,
      Validator validator) {
    Set<Element> list = Elements.elements().in(annotation);
    for (Element element : list) {
      if (element.isAnnotationPresent(Reference.class)) {
        Reference reference = element.getAnnotation(Reference.class);
        String propertyName = element.in(annotation).value();
        Element referenceElement = Elements.element(propertyName).in(parameter.target);
        //Validates the reference using the annotations present in the annotation property
        Object value = referenceElement.in(parameter.target).value();
        if (!isReferenceValid(element, value, binder)) {
          return false;
        }
        BindableElement bindableElement = Elements.element(reference.value()).forBind().in(validator);
        if (bindableElement != null) {
          if (bindableElement.type().isAssignableFrom(value.getClass())) {
            bindableElement.bind(value);
          } else {
            throw new IllegalArgumentException("Incompatible type. Reference: " + bindableElement.name()
                + "|Annotation: " + annotation.annotationType() + "|Element: " + parameter.element.name());
          }
        }
      }
    }
    return true;
  }

  /**
   * Validates the reference element using the specified annotations in the
   * annotation parameter
   */
  private boolean isReferenceValid(Element element, Object referenceValue, Binder binder) {
    for (Annotation annotation : element.getAnnotations()) {
      ValidatorContext context = new ValidatorContextImpl(annotation);
      if (validatorFactory.canCreate(context)) {
        Validator validator = validatorFactory.create(context);
        binder.applyBinds(validator);
        if (!validator.isValid(referenceValue)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isValid(Object value) {
    return validator.isValid(value);
  }

}
