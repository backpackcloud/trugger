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

package org.atatec.trugger.validation.validator;

import org.atatec.trugger.validation.ValidatorClass;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constraint that defines a minimum value that an element should have.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(MinValidator.class)
public @interface Min {

  /**
   * The minimum value that the element should have.
   */
  double value();

  /**
   * Sets if the minimum value is included in the valid range (in case of float
   * point values)
   */
  boolean inclusive() default true;

}
