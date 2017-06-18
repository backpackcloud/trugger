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
package tools.devnull.trugger.reflection;

import org.junit.Test;
import tools.devnull.trugger.Flag;

import static org.junit.Assert.assertEquals;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
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
        reflect().methods().in(TestObject.class).size()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertEquals(
        1,
        reflect().methods()
            .filter(method -> method.getName().equals("foo"))
            .in(TestObject.class)
            .size()
    );
  }

}
