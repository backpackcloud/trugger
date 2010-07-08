/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.formatter.formatters;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import net.sf.trugger.formatter.FormatterClass;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@FormatterClass(NumberFormatter.class)
public @interface Number {

  /**
   * Indicates the number type.
   */
  NumberType type() default NumberType.INT;

  /**
   * Indicates the pattern to use.
   */
  String pattern() default "";

  /**
   * Indicates the locale to use. Default to {@link Locale#getDefault()}.
   * <p>
   * To set the default locale, use
   * {@link NumberFormatter#setDefaultLocale(java.util.Locale)}.
   */
  String locale() default "";

}
