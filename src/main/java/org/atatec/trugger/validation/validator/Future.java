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
import java.util.Date;

import org.atatec.trugger.date.DateType;
import org.atatec.trugger.validation.ValidatorClass;

/**
 * Indicates that the value must be a future date.
 * <p>
 * This validation is only for {@link Date} objects.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(FutureValidator.class)
public @interface Future {

  /**
   * Indicates if the current date must be included.
   * <p>
   * Only makes sense if the comparison type is {@link DateType#DATE} or
   * {@link DateType#DATE_TIME}.
   */
  boolean includeToday() default true;

  /**
   * Indicates the type of comparison with the actual time.
   */
  DateType type() default DateType.DATE_TIME;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Future";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
