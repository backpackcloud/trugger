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

import org.atatec.trugger.HandlingException;
import org.junit.Test;
import org.kodo.TestScenario;

import static org.atatec.trugger.element.Elements.element;
import static org.kodo.Spec.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class HandleTest implements ElementSpecs {

  public static class TestObject {
    String string;
  }

  @Test
  public void testHandleForSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    TestScenario.given(element().in(obj))
        .the(value(), should(be("test")))
        .when(valueIsSetTo("other value"))
        .the(value(), should(be("other value")))
        .the(obj.string, should(be("other value")));
  }

  @Test
  public void testHandlerForNonSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    TestScenario.given(element().in(TestObject.class))
        .the(valueIn(obj), should(be("test")))
        .when(valueIsSetTo("other value", obj))
        .the(valueIn(obj), should(be("other value")))
        .the(obj.string, should(be("other value")));
  }

  @Test
  public void testHandlingError() {
    TestObject obj = new TestObject();
    TestScenario.given(element().in(obj))
        .then(attempToSetValueTo(10), should(raise(HandlingException.class)));
  }

}
