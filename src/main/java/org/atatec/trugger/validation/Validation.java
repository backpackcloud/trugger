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
package org.atatec.trugger.validation;

import org.atatec.trugger.annotation.AcceptedArrayTypes;
import org.atatec.trugger.annotation.AcceptedTypes;
import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.annotation.TargetAnnotation;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.annotation.TargetObject;
import org.atatec.trugger.loader.ImplementationLoader;

/**
 * This class uses a {@link ValidationEngine validation engine} for validate properties of
 * a target by using default implementations of the components required.
 * <p/>
 * There are a set of features that can be useful for developing a Validator and depends
 * on the implementations for the {@link ValidationEngine}, {@link ValidatorFactory} and
 * {@link ValidatorBinder} (all the listed features are present in the default
 * components):
 * <p/>
 * <ul> <li>If the validation must use a reference value present in the target object.
 * Just annotate a <i>String</i> property in the annotation that indicates the element
 * that must be binded with the {@link Reference} annotation. The reference can also have
 * their specific validation in the property of the annotation. <li>If the validation
 * requires direct access to the target, it will be bind in the element that has the
 * {@link TargetObject} annotation. <li>If the validation requires direct access to the
 * {@link ValidationEngine}, an instance of {@link ValidationBridge} will be injected in
 * the validator if there is a compatible element. <li>If the validator requires the
 * element that has being validated, this element will be injected in the element
 * annotated with {@link TargetElement}. <li>If the validator requires the annotation in
 * the element that has being validated, it will be injected in the compatible element of
 * the validator and/or in the element annotated with {@link TargetAnnotation} (useful for
 * generic validators). <li>To forbid a type that is not supported by the validator, you
 * can use the generic type (if the allowed type is only one) or the annotations {@link
 * AcceptedTypes} and/or {@link AcceptedArrayTypes} in the class declaration in case of
 * more than one. <li>If you need to use other validators inside yours, you can declare an
 * element of tipe {@link Validator} and annotate it with the desired validations. You can
 * use more than one annotation. <li>Validations may have a named context (configured by
 * the property "context" in any annotation). If a context is {@link
 * ValidationEngine#forContext(String) specified}, only the annotations with the specified
 * context or with no context will be processed. </ul>
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
public final class Validation {

  private static final ValidationFactory factory;

  static {
    factory = ImplementationLoader.instance().get(ValidationFactory.class);
  }

  private Validation() {
  }

  /**
   * Starts the configuration for the validation.
   *
   * @return a reference for the validation component.
   */
  public static ValidationEngine validation() {
    return factory.createValidationEngine(newValidatorFactory());
  }

  public static ValidatorFactory newValidatorFactory() {
    return factory.createValidatorFactory(newValidatorBinder());
  }

  public static ValidatorBinder newValidatorBinder() {
    return factory.createValidatorBinder();
  }

}
