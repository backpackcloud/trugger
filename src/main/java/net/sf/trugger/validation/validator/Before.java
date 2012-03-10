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
package net.sf.trugger.validation.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import net.sf.trugger.annotation.Reference;
import net.sf.trugger.date.DateType;
import net.sf.trugger.validation.ValidatorClass;

/**
 * Indicates that the value must be earlier than a reference Date.
 * <p>
 * This validation is only for {@link Date} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(BeforeValidator.class)
public @interface Before {

  /**
   * The name of the property of the target that will be the reference to
   * compare the value of the annotated property.
   */
  @NotNull
  @Reference
  String reference();

  /**
   * Indicates if the value is valid if equals the reference.
   */
  boolean validIfEquals() default false;

  /**
   * Indicates the type of comparison.
   */
  DateType type() default DateType.DATE_TIME;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Before";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
