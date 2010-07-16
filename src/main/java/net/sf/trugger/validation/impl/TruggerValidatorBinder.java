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
import net.sf.trugger.annotation.TargetAnnotation;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.annotation.TargetObject;
import net.sf.trugger.annotation.processors.TargetElementResolver;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.validation.InvalidReferenceException;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationContext;
import net.sf.trugger.validation.ValidationException;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorBinder;
import net.sf.trugger.validation.ValidatorContext;
import net.sf.trugger.validation.ValidatorFactory;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerValidatorBinder implements ValidatorBinder {

  private ValidatorFactory factory = Validation.newValidatorFactory();

  public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
    Annotation annotation = context.annotation();
    Class<? extends Annotation> annotationType = annotation.annotationType();
    binder.bind(annotation).toElement().ofType(annotationType);
    binder.bind(annotation).toElement().annotatedWith(TargetAnnotation.class);
    binder.bind(context.target()).toElement().annotatedWith(TargetObject.class);
    binder.use(new TargetElementResolver(context)).toElements().annotatedWith(TargetElement.class);
    binder.bind(context.validationContext()).toElement().annotatedWith(ValidationContext.class);
    bindReferences(context, binder, validator);
  }

  /**
   * Checks the references that the annotation needs. Binding the values in the
   * given validator.
   */
  private void bindReferences(ValidatorContext context, Binder binder, Validator validator) {
    Set<Element> list = Elements.elements().in(context.annotation());
    for (Element element : list) {
      if (element.isAnnotationPresent(Reference.class)) {
        Reference reference = element.getAnnotation(Reference.class);
        String propertyName = element.in(context.annotation()).value();
        Element referenceElement = Elements.element(propertyName).in(context.target());
        //Validates the reference using the annotations present in the annotation property
        Object value = referenceElement.in(context.target()).value();
        if (isReferenceValid(element, value, binder)) {
          bindReferenceValue(context, validator, reference, value);
        } else {
          throw new InvalidReferenceException();
        }
      }
    }
  }

  private void bindReferenceValue(ValidatorContext context, Validator validator, Reference reference, Object value) {
    //This bind is very specific for adding in the binder.
    BindableElement bindableElement = Elements.element(reference.value()).forBind().in(validator);
    if (bindableElement != null) {
      if (bindableElement.type().isAssignableFrom(value.getClass())) {
        bindableElement.bind(value);
      } else {
        throw new ValidationException("Incompatible type. Reference: " + bindableElement.name() + "|Annotation: "
            + context.annotation().annotationType() + "|Element: " + context.element().name());
      }
    }
  }

  /**
   * Validates the reference element using the specified annotations in the
   * annotation parameter
   */
  private boolean isReferenceValid(Element element, Object referenceValue, Binder binder) {
    for (Annotation annotation : element.getAnnotations()) {
      ValidatorContext context = new ValidatorContextImpl(annotation);
      if (factory.canCreate(context)) {
        Validator validator = factory.create(context);
        binder.applyBinds(validator);
        if (!validator.isValid(referenceValue)) {
          return false;
        }
      }
    }
    return true;
  }

}
