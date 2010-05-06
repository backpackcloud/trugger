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
package net.sf.trugger.validation.validator;

import java.util.Calendar;
import java.util.Date;

import net.sf.trugger.date.DateType;
import net.sf.trugger.validation.Validator;

/**
 * The implementation of the {@link After} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class AfterValidator implements Validator<Date> {

  private Date reference;
  private After annotation;

  public boolean isValid(@NotNull Date value) {
    Calendar cal = Calendar.getInstance();
    Calendar referenceCal = Calendar.getInstance();

    cal.setTime(value);
    referenceCal.setTime(reference);

    DateType type = annotation.type();
    type.clearIrrelevantFields(cal);
    type.clearIrrelevantFields(referenceCal);

    boolean after = cal.after(referenceCal);
    if (!after && annotation.validIfEquals()) {
      after |= cal.equals(referenceCal);
    }
    return after;
  }

}
