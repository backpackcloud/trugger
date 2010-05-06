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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import net.sf.trugger.validation.ValidRegion;
import net.sf.trugger.validation.validator.Range;

import org.junit.Test;

public class RangeValidatorTest extends ValidatorTest<Range> {

  @Test
  public void testIncludeMinAndNotMaxAndValidRegionIn() {
    builder.map(5.0).to(annotation.min());
    builder.map(10.5).to(annotation.max());
    builder.map(true).to(annotation.includeMin());
    builder.map(false).to(annotation.includeMax());
    builder.map(0.0001).to(annotation.delta());
    createValidator();

    assertValid(BigInteger.valueOf(5));
    assertValid(9.3);
    assertValid(4.9999);
    assertValid(BigDecimal.valueOf(10.4999));
    assertInvalid(4.9998);
    assertValid(10.4998);
    assertInvalid(BigDecimal.valueOf(10.5001));
  }

  @Test
  public void testIncludeMinAndNotMaxAndValidRegionOut() {
    builder.map(5.0).to(annotation.min());
    builder.map(10.5).to(annotation.max());
    builder.map(true).to(annotation.includeMin());
    builder.map(false).to(annotation.includeMax());
    builder.map(0.0001).to(annotation.delta());
    builder.map(ValidRegion.OUT).to(annotation.validRegion());
    createValidator();

    assertValid(5.0);
    assertInvalid(9.3);
    assertValid(4.9999);
    assertInvalid(10.4999);
    assertValid(4.9998);
    assertInvalid(5.0002);
    assertInvalid(10.4998);
    assertValid(10.5001);

    assertValid(null);

    assertTypeDisallowed(false);
    assertTypeDisallowed(new Date());
  }

  @Test
  public void testIncludeMinAndMaxAndValidRegionIn() {
    builder.map(5.0).to(annotation.min());
    builder.map(10.5).to(annotation.max());
    builder.map(true).to(annotation.includeMin());
    builder.map(true).to(annotation.includeMax());
    builder.map(0.0001).to(annotation.delta());
    createValidator();
    assertValid(BigDecimal.valueOf(10.5001));
    assertInvalid(BigDecimal.valueOf(10.6));
    assertInvalid(BigDecimal.valueOf(10.501));
  }

}
