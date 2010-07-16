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
package net.sf.trugger.test.format;

import net.sf.trugger.format.formatters.Mask;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MaskFormatterTest extends FormatterTest<Mask> {

  @Test
  public void testFormat() throws Exception {
    builder.map("##.##/##").to(annotation.value());

    assertFormat("12.34/56", "123456");
    assertFormatError("1222.35/56");
  }

  @Test
  public void testParse() throws Exception {
    builder.map("##.##").to(annotation.value());

    assertParse("1234", "12.34");
    assertParseError("12.245");
  }

}
