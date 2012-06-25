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
package org.atatec.trugger.test.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.atatec.trugger.date.DateType;
import org.atatec.trugger.message.Message;
import org.atatec.trugger.message.MessagePart;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationResult;
import org.atatec.trugger.validation.ValidatorClass;
import org.atatec.trugger.validation.validator.After;
import org.atatec.trugger.validation.validator.GroupValidator;
import org.atatec.trugger.validation.validator.NotEmpty;
import org.atatec.trugger.validation.validator.NotNull;
import org.atatec.trugger.validation.validator.Range;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MessagesTest {
  
  @NotNull
  @NotEmpty
  @ValidatorClass(GroupValidator.class)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface NotNullAndNotEmpty {
    
    String message() default "not null & not empty";
    
  }
  
  public static class TestObject {
    
    @Range(min = 0.45, max = 7.89)
    public double value;
    
    @After(reference = "date2", type = DateType.DATE)
    public Date date1;
    
    public Date date2;
    
    @NotNullAndNotEmpty
    public String string;
    
  }
  
  private ValidationResult result;
  private TestObject testObject;
  
  @Before
  public void initialize() {
    Validation validation =
        new Validation(ResourceBundle.getBundle("org.atatec.trugger.test.validation.validation-messages", Locale.ENGLISH));
    testObject = new TestObject();
    
    testObject.value = 0.004;
    Calendar cal = Calendar.getInstance();
    DateType.DATE.clearIrrelevantFields(cal);
    testObject.date1 = cal.getTime();
    cal.add(Calendar.DATE, 1);
    testObject.date2 = cal.getTime();
    
    result = validation.validate().allElements().in(testObject);
  }
  
  @Test
  public void testMessageFormatter() {
    assertFalse(result.isValid());
    
    Collection<InvalidElement> invalids = result.invalidElements();
    for (InvalidElement invalid : invalids) {
      List<Message> messages = invalid.messages();
      String summary = messages.get(0).getSummary();
      String detail = messages.get(0).getDetail();
      if (invalid.name().equals("string")) {
        assertEquals(3, messages.size());
      } else {
        assertEquals(1, messages.size());
      }
      String name = invalid.name();
      if (name.equals("value")) {
        assertEquals("The value 0.004 is out of range", summary);
        assertEquals("0.004 is out of range [0.45 ; 7.89]", detail);
      } else if (name.equals("date1")) {
        assertEquals("The reference is after the value", summary);
        assertEquals("DATE2 | DATE_TYPE | date2", detail);
      }
    }
  }
  
  @Test
  public void testJoinMessages() {
    InvalidElement el = result.invalidElement("string");
    assertEquals("not null|not empty|not null & not empty", el.joinMessages("|", MessagePart.SUMMARY));
    assertEquals("not null detail|not empty detail", el.joinMessages("|", MessagePart.DETAIL));
  }
  
}
