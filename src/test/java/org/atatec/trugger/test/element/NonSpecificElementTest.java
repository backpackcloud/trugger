/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.test.element;

import static org.atatec.trugger.element.Elements.element;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.NonSpecificElementException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NonSpecificElementTest {

  private Element element;

  @Before
  public void init() {
    element = element("element").in(NonSpecificElementTest.class);
  }

  @Test(expected = NonSpecificElementException.class)
  public void testRead() throws Exception {
    element.value();
  }

  @Test(expected = NonSpecificElementException.class)
  public void testWrite() throws Exception {
    element.value(null);
  }

}
