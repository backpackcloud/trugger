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
package net.sf.trugger.validation;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;

/**
 * Interface that defines a class that serves as a bridge between the
 * {@link Validator} and the {@link ValidationEngine}.
 * <p>
 * An implementation of this interface is automatically binded to a
 * {@link Validator} if it declares an {@link BindableElement element} of this
 * type.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface ValidationBridge {

  /**
   * Adds the given invalid property in the collection of invalid properties
   * handled by the validator.
   *
   * @param invalidElement
   *          the invalid element
   */
  void addInvalidElement(InvalidElement invalidElement);

  /**
   * Adds the given invalid properties in the collection of invalid properties
   * handled by the validator.
   *
   * @param invalidElements
   *          the invalid elements
   */
  void addInvalidElements(Collection<InvalidElement> invalidElements);

  /**
   * Starts a new Validation using the same components configuration.
   *
   * @return the component for doing the validation.
   */
  ValidationEngine validate();

  /**
   * @param element
   *          the element that contains annotations for validations.
   * @return a validator for encapsulating all validations present in the given
   *         element.
   */
  Validator createValidator(Element element);

  /**
   * Validates the given value using the annotations present in the given
   * annotated element. The messages will be added to the current element in
   * validation.
   *
   * @return <code>true</code> if the value is valid, <code>false</code>
   *         otherwise.
   */
  boolean validate(AnnotatedElement annotatedElement, Object value);

}
