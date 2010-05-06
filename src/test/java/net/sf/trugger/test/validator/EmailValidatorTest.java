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
package net.sf.trugger.test.validator;

import static net.sf.trugger.validation.validator.Email.EmailValidationMethod.RFC_2822;
import static net.sf.trugger.validation.validator.Email.EmailValidationMethod.RFC_2822_SIMPLIFIED;
import net.sf.trugger.validation.validator.Email;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class EmailValidatorTest extends ValidatorTest<Email> {

  @Test
  public void testSimplifiedMethodValidator() {
    builder.map(RFC_2822_SIMPLIFIED).to(annotation.method());
    createValidator();

    basicEmailAssert();
    assertInvalid("mymail@[150.120.154.124]");
  }

  @Test
  public void testFullMethodValidator() {
    builder.map(RFC_2822).to(annotation.method());
    createValidator();
    basicEmailAssert();
    assertValid(new StringBuilder("mymail@[150.120.154.124]"));
    assertInvalid("mymail@[150.120.154.256]");

    assertTypeDisallowed(new Object());
    assertTypeDisallowed(12);
  }

  private void basicEmailAssert() {
    assertValid(null);
    assertValid("mymail@myserver.com");
    assertInvalid("mymail@myserver");
    assertValid("mymail@myserver.c");
    assertInvalid("mymail @ myserver.c");
    assertInvalid("mymail @ myserver.c ");
  }
}
