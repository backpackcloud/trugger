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
package net.sf.trugger.format.formatters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import net.sf.trugger.ParseException;
import net.sf.trugger.bind.PostBind;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.validation.validator.NotEmpty;
import net.sf.trugger.validation.validator.NotNull;

/**
 * Formatter for {@link Number}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class NumberFormatter implements Formatter<java.lang.Number> {

  private static Locale defaultLocale = Locale.getDefault();

  private Number annotation;
  private NumberFormat numberFormat;

  @PostBind
  private void init() {
    Locale locale;
    String localeArgs = annotation.locale();
    if (localeArgs.isEmpty()) {
      locale = defaultLocale;
    } else {
      String[] args = localeArgs.split("_");
      switch (args.length) {
        case 1:
          locale = new Locale(args[0]);
          break;
        case 2:
          locale = new Locale(args[0], args[1]);
          break;
        default:
          throw new IllegalArgumentException("Invalid locale " + localeArgs);
      }
    }
    numberFormat = new DecimalFormat(annotation.pattern(), DecimalFormatSymbols.getInstance(locale));
  }

  @Override
  public String format(@NotNull java.lang.Number value) {
    return numberFormat.format(value);
  }

  @Override
  public java.lang.Number parse(@NotEmpty String value) throws ParseException {
    try {
      java.lang.Number number = numberFormat.parse(value);
      switch (annotation.type()) {
        case INT:
          return number.intValue();
        case LONG:
          return number.longValue();
        case FLOAT:
          return number.floatValue();
        case DOUBLE:
          return number.doubleValue();
        case BIG_INT:
          return BigInteger.valueOf(number.longValue());
        case BIG_DECIMAL:
          return BigDecimal.valueOf(number.doubleValue());
        default:
          throw new IllegalArgumentException();
      }
    } catch (java.text.ParseException e) {
      throw new ParseException(e);
    }
  }

  /**
   * Sets the default locale to use when no one is specified.
   *
   * @param defaultLocale
   *          the default locale to use
   */
  public static void setDefaultLocale(Locale defaultLocale) {
    NumberFormatter.defaultLocale = defaultLocale;
  }

}
