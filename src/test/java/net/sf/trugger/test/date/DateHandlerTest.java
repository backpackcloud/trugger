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
package net.sf.trugger.test.date;

import static net.sf.trugger.date.DateHandler.now;
import static net.sf.trugger.date.DateHandler.timeNow;
import static net.sf.trugger.date.DateHandler.today;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import net.sf.trugger.date.DateType;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class DateHandlerTest {

  @Test
  public void test() {
    Calendar cal = Calendar.getInstance();
    DateType.DATE_TIME.clearIrrelevantFields(cal);
    assertEquals(cal.getTime(), now());

    cal = Calendar.getInstance();
    DateType.DATE.clearIrrelevantFields(cal);
    assertEquals(cal.getTime(), today());

    cal = Calendar.getInstance();
    DateType.TIME.clearIrrelevantFields(cal);
    assertEquals(cal.getTime(), timeNow());
  }

}
