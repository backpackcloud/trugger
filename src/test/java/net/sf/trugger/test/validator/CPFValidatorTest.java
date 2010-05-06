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

import java.util.Date;

import net.sf.trugger.validation.validator.pt.br.CPF;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class CPFValidatorTest extends ValidatorTest<CPF> {

  @Test
  public void testTypes() {
    assertTypeDisallowed(new Date());
    assertValid(null);
    assertValid("");
  }

  @Test
  public void testValidNonMaskedCPF() {
    assertValid("22233366638");
    assertValid("01234567890");
  }

  @Test
  public void testValidMaskedCPF() {
    assertValid("012.345.678-90");
    assertValid("222.333.666-38");
  }

  @Test
  public void testInvalidNonMaskedCPF() {
    assertInvalid("54561234878");
    assertInvalid("2468949743A");
    assertInvalid("82469084568");
    assertInvalid("56957386907");
  }

  @Test
  public void testInvalidMaskedCPF() {
    assertInvalid("012-345-678-90");
    assertInvalid("545.612.348-78");
    assertInvalid("246.894.974-37");
    assertInvalid("824.690.845-68");
    assertInvalid("569.573.869-07");
  }

  @Test
  public void testSameDigitsCPF() {
    assertInvalid("11111111111");
    assertInvalid("22222222222");
    assertInvalid("33333333333");
    assertInvalid("44444444444");
    assertInvalid("55555555555");
    assertInvalid("66666666666");
    assertInvalid("77777777777");
    assertInvalid("88888888888");
    assertInvalid("99999999999");
  }

}
