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

/**
 * Interface that defines a factory to create {@link Validator} objects.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface ValidatorFactory {

  /**
   * Creates a validator that the given constraint defines.
   *
   * @param annotation the constraint
   * @return the validator or <code>null</code> if the annotation does not
   * define a validator.
   */
  Validator create(Annotation annotation);

  /**
   * Creates a validator based on the given constraint and context objects.
   *
   * The context objects will be used as dependencies to inject.
   *
   * @param annotation  the constraint
   * @param element     the element being validated
   * @param target      the target being validated
   * @param engine      the engine to inject if the validator requires one
   * @return the validator or <code>null</code> if the annotation does not
   * define a validator.
   */
  Validator create(Annotation annotation,
                   Element element,
                   Object target,
                   ValidationEngine engine);

}
