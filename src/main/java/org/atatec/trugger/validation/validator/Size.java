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

import org.atatec.trugger.validation.ValidatorClass;

/**
 * Validates a size of an object that must be a Collection, a Map or an Array.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(SizeValidator.class)
public @interface Size {

  /**
   * The minimum size that makes the object valid.
   */
  int min() default 0;

  /**
   * The maximum size that makes the object valid.
   */
  int max() default Integer.MAX_VALUE;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Size";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
