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

import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.date.DateType;
import org.atatec.trugger.validation.ValidatorClass;

/**
 * Indicates that the annotated date must be later than a reference date.
 * <p>
 * This validation is only for {@link Date} objects.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(AfterValidator.class)
public @interface After {

  /**
   * The name of the property of the target that will be the reference to
   * compare compare the value of the annotated property.
   */
  @NotNull
  @Reference
  String reference();

  /**
   * Indicates the type of comparison.
   */
  DateType type() default DateType.DATE_TIME;

  /**
   * Indicates if the value is valid if equals the reference.
   */
  boolean validIfEquals() default false;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.After";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
