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
package org.atatec.trugger.test.validator;

import java.util.Calendar;

import org.atatec.trugger.validation.validator.IPAddress;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class IPAddressValidatorTest extends ValidatorTest<IPAddress> {

  @Test
  public void testValidator() {
    createValidator();

    assertValid(null);
    assertValid("42.6.7.56");
    assertValid("172.35.75.255");
    assertValid("172.35.255.75");
    assertValid("172.255.35.75");
    assertValid("255.35.75.172");
    assertValid("127.0.0.1");

    assertInvalid("426756");
    assertInvalid("152.256.0.1");
    assertInvalid("256.253.13.16");
    assertInvalid("253.256.13.16");
    assertInvalid("253.13.256.16");
    assertInvalid("253.13.16.256");

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(17549);
    assertTypeDisallowed(Calendar.getInstance());
  }

}
