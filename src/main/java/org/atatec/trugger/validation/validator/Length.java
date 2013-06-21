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
 * Indicates the accepted length of a value.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(LengthValidator.class)
public @interface Length {

  /**
   * The minimum value.
   */
  int min() default 0;

  /**
   * The maximum value
   */
  int max() default Integer.MAX_VALUE;

  /**
   * Indicates if the value must be {@link String#trim() trimmed} before the
   * validation.
   */
  boolean trim() default true;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Length";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
