/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger.element;

import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Map;

import static io.backpackcloud.kodo.Expectation.to;
import static io.backpackcloud.trugger.element.Elements.element;

/**
 * @author Marcelo Guimaraes
 */
public class NonSpecificElementTest implements ElementExpectations {

  private Element nonSpecificElement =
      element("nonSpecificElement").from(Map.class).get();

  @Test
  public void testRead() throws Exception {
    Spec.given(nonSpecificElement)
        .expect(getValue(), to().raise(NonSpecificElementException.class));
  }

  @Test
  public void testWrite() throws Exception {
    Spec.given(nonSpecificElement)
        .expect(attempToChangeValue(), to().raise(NonSpecificElementException.class));
  }

}
