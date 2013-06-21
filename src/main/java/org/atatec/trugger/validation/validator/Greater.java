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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.validation.ValidatorClass;

/**
 * Indicates that a value must be greater than another one.
 *
 * @author Marcelo Guimarães
 * @since 2.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(GreaterValidator.class)
public @interface Greater {

  /**
   * The reference value.
   */
  @NotNull
  @Reference
  String than();

  /**
   * Indicates if the value can be equal.
   */
  boolean orEqual() default false;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Greater";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
