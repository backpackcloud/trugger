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

import static net.sf.trugger.util.mock.Mock.element;
import static net.sf.trugger.util.mock.Mock.mock;
import net.sf.trugger.element.Element;
import net.sf.trugger.format.formatters.Number;
import net.sf.trugger.format.formatters.NumberType;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NumberFormatterTest extends FormatterTest<Number> {

  @Test
  public void testFormatWithElement() throws Exception {
    Element element = mock(element().ofType(double.class));
    builder.map(NumberType.INT).to(annotation.type());
    builder.map("en_US").to(annotation.locale());
    builder.map("0.00").to(annotation.pattern());
    createFormatter(element);

    assertFormat("25.50", 25.5);
  }

  @Test
  public void testFomatWithoutElement() throws Exception {
    builder.map(NumberType.DOUBLE).to(annotation.type());
    builder.map("en_US").to(annotation.locale());
    builder.map("0.00").to(annotation.pattern());

    assertFormat("25.50", 25.5);
  }

  @Test
  public void testParseWithElement() throws Exception {
    Element element = mock(element().ofType(double.class));
    builder.map(NumberType.INT).to(annotation.type());
    builder.map("en_US").to(annotation.locale());
    builder.map("0.00").to(annotation.pattern());

    createFormatter(element);

    assertParse(25.5, "25.50");
    assertParseError("$25.555");
  }

  @Test
  public void testParseWithoutElement() throws Exception {
    builder.map(NumberType.DOUBLE).to(annotation.type());
    builder.map("en_US").to(annotation.locale());
    builder.map("0.00").to(annotation.pattern());

    assertParse(25.5, "25.50");
    assertParseError("$25.555");
  }

}
