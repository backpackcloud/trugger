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
package net.sf.trugger.test.validator;

import java.util.Date;

import net.sf.trugger.validation.validator.pt.br.CEP;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class CEPValidatorTest extends ValidatorTest<CEP> {

  @Test
  public void testTypes() {
    assertTypeDisallowed(new Date());
    assertValid(null);
    assertValid("");
  }

  @Test
  public void testValidNonMaskedCEP() {
    assertValid("70502689");
    assertValid("99999999");
    assertValid("54687358");
  }

  @Test
  public void testValidMaskedCEP() {
    assertValid("56432-595");
    assertValid("24635-461");
    assertValid("99999-999");
  }

  @Test
  public void testInvalidNonMaskedCEP() {
    assertInvalid("54561234878");
    assertInvalid("5695738690A");
  }

  @Test
  public void testInvalidMaskedCEP() {
    assertInvalid("012-345-678-90");
    assertInvalid("12346-8562");
    assertInvalid("12.345-862");
  }

}
