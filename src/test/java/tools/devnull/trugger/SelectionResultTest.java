/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;

public class SelectionResultTest {

  private Object result = new Object();
  private Object target = new Object();

  @Test
  public void testSelectionWithResult() {
    SelectionResult<Object> selectionResult = new SelectionResult<>(target, result);

    assertSame(result, selectionResult.result());
    assertNotNull(selectionResult.value());
  }

  @Test
  public void testSelectionWithoutResult() {
    SelectionResult<Object> selectionResult = new SelectionResult<>(target, null);

    assertNull(selectionResult.result());
    assertNull(selectionResult.value());
  }

}
