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
package net.sf.trugger.test.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationResult;
import net.sf.trugger.validation.validator.NotNull;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ContextValidationTest {
  
  public static class MyClass {
    
    @NotNull
    String notNull;
    
    @NotNull(context = "basic")
    String notNullForBasic;
    
    @NotNull(context = { "minimal", "basic" })
    String notNullForMinimalAndBasic;
    
    public MyClass(boolean valid) {
      if(valid) {
        notNull = "";
        notNullForBasic = "";
        notNullForMinimalAndBasic = "";
      }
    }
    
  }
  
  @Test
  public void testInvalidWithNoContext() {
    MyClass c = new MyClass(false);
    ValidationResult result = new Validation().validate().allElements().in(c);
    assertTrue(result.isInvalid());
    assertEquals(3, result.invalidElements().size());
    assertNotNull(result.invalidElement("notNull"));
    assertNotNull(result.invalidElement("notNullForBasic"));
    assertNotNull(result.invalidElement("notNullForMinimalAndBasic"));
  }
  
  @Test
  public void testInvalidWithContexts() {
    MyClass c = new MyClass(false);
    ValidationResult result = new Validation().validate().forContext("basic").allElements().in(c);
    assertTrue(result.isInvalid());
    assertEquals(3, result.invalidElements().size());
    assertNotNull(result.invalidElement("notNull"));
    assertNotNull(result.invalidElement("notNullForBasic"));
    assertNotNull(result.invalidElement("notNullForMinimalAndBasic"));
    
    result = new Validation().validate().forContext("minimal").allElements().in(c);
    assertTrue(result.isInvalid());
    assertEquals(2, result.invalidElements().size());
    assertNotNull(result.invalidElement("notNull"));
    assertNotNull(result.invalidElement("notNullForMinimalAndBasic"));
  }
  
  @Test
  public void testValidWithNoContext() {
    MyClass c = new MyClass(true);
    ValidationResult result = new Validation().validate().allElements().in(c);
    assertTrue(result.isValid());
    assertEquals(0, result.invalidElements().size());
    assertNull(result.invalidElement("notNull"));
    assertNull(result.invalidElement("notNullForBasic"));
    assertNull(result.invalidElement("notNullForMinimalAndBasic"));
  }
  
  @Test
  public void testValidWithContexts() {
    MyClass c = new MyClass(true);
    ValidationResult result = new Validation().validate().forContext("basic").allElements().in(c);
    assertTrue(result.isValid());
    assertEquals(0, result.invalidElements().size());
    assertNull(result.invalidElement("notNull"));
    assertNull(result.invalidElement("notNullForBasic"));
    assertNull(result.invalidElement("notNullForMinimalAndBasic"));
    
    result = new Validation().validate().forContext("minimal").allElements().in(c);
    assertTrue(result.isValid());
    assertEquals(0, result.invalidElements().size());
    assertNull(result.invalidElement("notNull"));
    assertNull(result.invalidElement("notNullForBasic"));
    assertNull(result.invalidElement("notNullForMinimalAndBasic"));
  }
  
}
