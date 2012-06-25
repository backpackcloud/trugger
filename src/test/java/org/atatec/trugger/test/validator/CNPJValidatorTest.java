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

import java.util.Date;

import org.atatec.trugger.validation.validator.pt.br.CNPJ;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class CNPJValidatorTest extends ValidatorTest<CNPJ> {

  @Test
  public void testTypes() {
    assertTypeDisallowed(new Date());
    assertValid(null);
    assertValid("");
  }

  @Test
  public void testValidNonMaskedCNPJ() {
    assertValid("11222333000181");
  }

  @Test
  public void testValidMaskedCNPJ() {
    assertValid("11.222.333/0001-81");
  }

  @Test
  public void testInvalidNonMaskedCNPJ() {
    assertInvalid("11222334000182");
    assertInvalid("11222333000X82");
    assertInvalid("11222333000183");
  }

  @Test
  public void testInvalidMaskedCNPJ() {
    assertInvalid("11.222.333/0001.81");
    assertInvalid("11.222-333/0001-81");
    assertInvalid("11.222.334/0001-82");
    assertInvalid("11.222.333/0002-82");
    assertInvalid("11.222.333/0001-83");
  }

}
