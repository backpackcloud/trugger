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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.trugger.bind.PostBind;
import net.sf.trugger.formatter.Formatter;
import net.sf.trugger.validation.validator.NotEmpty;
import net.sf.trugger.validation.validator.NotNull;

/**
 * Implementation for {@link DateFormat} format.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class DateFormatter implements Formatter<Date> {

  private DateFormat annotation;
  private SimpleDateFormat dateFormat;

  @PostBind
  private void init() {
    dateFormat = new SimpleDateFormat(annotation.value());
  }

  @Override
  public String format(@NotNull Date value) {
    return dateFormat.format(value);
  }

  @Override
  public Date parse(@NotEmpty String value) {
    try {
      return dateFormat.parse(value);
    } catch (ParseException e) {
      throw new net.sf.trugger.ParseException(e);
    }
  }

}
