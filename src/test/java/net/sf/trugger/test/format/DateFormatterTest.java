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

import java.util.GregorianCalendar;

import net.sf.trugger.format.formatters.Date;

import org.junit.Test;


/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class DateFormatterTest extends FormatterTest<Date> {

  @Test
  public void testFormat() throws Exception {
    builder.map("dd/MM/yyyy").to(annotation.value());

    assertFormat("10/05/2010", new GregorianCalendar(2010, 4, 10).getTime());
  }

}
