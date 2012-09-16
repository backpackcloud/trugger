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
package org.atatec.trugger.validation.impl;

import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.annotation.TargetAnnotation;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.annotation.TargetObject;
import org.atatec.trugger.annotation.processors.TargetElementResolver;
import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.bind.Binder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.validation.InvalidReferenceException;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationContext;
import org.atatec.trugger.validation.ValidationException;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorBinder;
import org.atatec.trugger.validation.ValidatorContext;
import org.atatec.trugger.validation.ValidatorFactory;
import org.atatec.trugger.validation.ValidatorInvoker;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerValidatorBinder implements ValidatorBinder {

  private ValidatorFactory factory = Validation.newValidatorFactory();

  public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
    Annotation annotation = context.annotation();
    Class<? extends Annotation> annotationType = annotation.annotationType();
    binder
      .bind(annotation).to(element().ofType(annotationType))
      .bind(annotation).to(element().annotatedWith(TargetAnnotation.class))
      .bind(context.target()).to(element().annotatedWith(TargetObject.class))
      .use(new TargetElementResolver(context)).in(elements().annotatedWith(TargetElement.class))
      .bind(context.validationContext()).to(element().annotatedWith(ValidationContext.class));

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
        ValidatorInvoker invoker = factory.create(context);
        binder.applyIn(invoker.validator());
        if (!invoker.isValid(referenceValue)) {
          return false;
        }
      }
    }
    return true;
  }

}
