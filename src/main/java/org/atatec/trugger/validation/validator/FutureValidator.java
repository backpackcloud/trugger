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
package org.atatec.trugger.validation.validator;

import java.util.Calendar;
import java.util.Date;

import org.atatec.trugger.date.DateType;
import org.atatec.trugger.validation.Validator;

/**
 * Implementation of the {@link Future} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class FutureValidator implements Validator<Date> {

  private Future annotation;

  public boolean isValid(@NotNull Date value) {
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(value);
    DateType type = annotation.type();
    type.clearIrrelevantFields(cal);
    type.clearIrrelevantFields(cal2);
    boolean valid = cal2.after(cal);
    return !valid && annotation.includeToday() ? cal2.equals(cal) : valid;
  }

}
