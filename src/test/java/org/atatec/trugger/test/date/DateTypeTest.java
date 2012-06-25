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
package org.atatec.trugger.test.date;

import static org.atatec.trugger.date.DateType.DATE;
import static org.atatec.trugger.date.DateType.DATE_TIME;
import static org.atatec.trugger.date.DateType.TIME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class DateTypeTest {
  
  @Test
  public void typeDateTest() {
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    
    cal.set(Calendar.HOUR_OF_DAY, 4);
    cal2.set(Calendar.HOUR_OF_DAY, 8);
    
    DATE.clearIrrelevantFields(cal);
    DATE.clearIrrelevantFields(cal2);
    
    assertTrue(cal.equals(cal2));
    
    cal.set(Calendar.DATE, 3);
    cal2.set(Calendar.DATE, 5);
    
    DATE.clearIrrelevantFields(cal2);
    assertFalse(cal.equals(cal2));
  }
  
  @Test
  public void typeTimeTest() {
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    
    cal.set(Calendar.DAY_OF_YEAR, 10);
    cal2.setTimeInMillis(cal.getTimeInMillis());
    cal2.set(Calendar.DAY_OF_YEAR, 50);
    
    TIME.clearIrrelevantFields(cal);
    TIME.clearIrrelevantFields(cal2);
    
    assertTrue(cal.equals(cal2));
    
    cal2.add(Calendar.MINUTE, 5);
    TIME.clearIrrelevantFields(cal2);
    assertFalse(cal.equals(cal2));
  }
  
  @Test
  public void typeDateTimeTest() {
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    
    cal2.setTimeInMillis(cal.getTimeInMillis() * 2);
    
    DATE_TIME.clearIrrelevantFields(cal);
    DATE_TIME.clearIrrelevantFields(cal2);
    
    assertFalse(cal.equals(cal2));
    assertTrue(cal.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND));
  }
  
}
