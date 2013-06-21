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

import org.atatec.trugger.util.Utils;
import org.atatec.trugger.validation.ValidatorClass;

/**
 * Indicates that the value could not be empty. There is three rules to this
 * validation based on the type of the value:
 * <ul>
 * <li>A <code>null</code> value.
 * <li>String - cannot be empty based on the return of the method
 * {@link Utils#isEmpty(CharSequence)}.
 * <li>Collection, Map e array - the size cannot be <code>zero</code>.
 * </ul>
 * <p>
 * Note that <code>null</code> values invalidates the property.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(NotEmptyValidator.class)
public @interface NotEmpty {

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.NotEmpty";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
