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
package com.backpackcloud.trugger.element;

import com.backpackcloud.trugger.HandlingException;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import static io.backpackcloud.kodo.Expectation.the;
import static io.backpackcloud.kodo.Expectation.to;
import static com.backpackcloud.trugger.element.Elements.element;

/**
 * @author Marcelo Guimaraes
 */
public class HandleTest implements ElementExpectations {

  public static class TestObject {
    String string;
  }

  @Test
  public void testHandleForSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    Spec.given(element().from(obj).get())
        .expect(Element::getValue, to().be("test"))
        .when(valueIsSetTo("other value"))
        .expect(Element::getValue, to().be("other value"))
        .expect(the(obj.string), to().be("other value"));
  }

  @Test
  public void testHandlerForNonSpecificElements() {
    TestObject obj = new TestObject();
    obj.string = "test";

    Spec.given(element().from(TestObject.class).get())
        .expect(valueIn(obj), to().be("test"))
        .when(valueIsSetTo("other value", obj))
        .expect(valueIn(obj), to().be("other value"))
        .expect(the(obj.string), to().be("other value"));
  }

  @Test
  public void testHandlingError() {
    TestObject obj = new TestObject();
    Spec.given(element().from(obj).get())
        .expect(settingValueTo(10), to().raise(HandlingException.class));
  }

}
