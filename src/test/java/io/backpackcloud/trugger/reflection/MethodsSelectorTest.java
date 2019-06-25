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
package io.backpackcloud.trugger.reflection;

import org.junit.Test;
import io.backpackcloud.trugger.Flag;

import static org.junit.Assert.assertEquals;

/**
 * @author Marcelo Guimaraes
 */
public class MethodsSelectorTest {

  static class TestObject {
    @Flag
    void foo() {
    }

    void bar() {
    }

  }

  @Test
  public void testNoSelector() throws Exception {
    assertEquals(
        2,
        Reflection.reflect().methods().from(TestObject.class).size()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertEquals(
        1,
        Reflection.reflect().methods()
            .filter(method -> method.getName().equals("foo"))
            .from(TestObject.class)
            .size()
    );
  }

}
