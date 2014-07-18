/*
 * Copyright 2009-2014 Marcelo Guimarães
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

package tools.devnull.trugger.validation;

import tools.devnull.trugger.element.Element;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Interface that represents an invalid element.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface InvalidElement extends Element {

  /**
   * Returns the invalid value.
   * <p>
   * Note that this method may not return the same value as {@link #value()}
   * because the element value may change after the validation.
   *
   * @return the invalid value of the element
   */
  Object invalidValue();

  /**
   * Returns a list of the annotation that defines constraints violated by this
   * element.
   * <p>
   * The annotations can be used to create validation messages.
   *
   * @return a list of the annotation that defines constraints violated by this
   * element.
   */
  Collection<Annotation> violatedConstraints();

  /**
   * @param constraint the constraint annotation type
   * @return the violated constraint or <code>null</code> if the constraint
   * is not present in the element or it was not violated.
   */
  Annotation violatedConstraint(Class<? extends Annotation> constraint);

  /**
   * @param constraint the constraint annotation type
   * @return <code>true</code> if the given constraint is violated in this
   * element or <code>false</code> if is not defined or violated.
   */
  boolean isConstraintViolated(Class<? extends Annotation> constraint);

}
