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
package org.atatec.trugger.test.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.atatec.trugger.annotation.AcceptedTypes;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.annotation.TargetObject;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.NotNull;

/**
 * @author Marcelo Varella Barca Guimarães
 */
@AcceptedTypes( { String.class, Date.class })
public class CustomValidator implements Validator<Object> {
  
  @TargetObject
  private Ticket target;
  
  @TargetElement
  private Date date;
  
  @TargetElement
  private Element property;
  
  @TargetElement("arrival")
  private Element otherProperty;
  
  @NotNull
  private Validator validator;
  
  public boolean isValid(Object value) {
    assertNotNull(target);
    assertNotNull(property);
    assertNotNull(validator);
    
    assertEquals("arrival", property.name());
    
    assertEquals(target.getArrival(), property.in(target).value());
    assertEquals(target.getArrival(), otherProperty.in(target).value());
    assertEquals(target.getArrival(), date);
    
    assertTrue(validator.isValid(new Object()));
    assertFalse(validator.isValid(null));
    
    return true;
  }
  
}
