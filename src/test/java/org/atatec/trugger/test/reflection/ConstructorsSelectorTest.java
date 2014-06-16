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
package org.atatec.trugger.test.reflection;

import org.atatec.trugger.test.Flag;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.atatec.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorsSelectorTest {

  static final Predicate ALL = el -> true;
  static final Predicate NONE = el -> false;

  static class TestObject {
    @Flag
    TestObject() {
    }

    public TestObject(int i) {
    }
  }

  @Test
  public void testNoSelector() {
    assertEquals(
        2,
        reflect().constructors().in(TestObject.class).size()
    );
    assertEquals(
        1,
        reflect().visible().constructors().in(TestObject.class).size()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertEquals(
        2,
        reflect().constructors().filter(ALL).in(TestObject.class).size()
    );
    assertEquals(
        1,
        reflect().visible().constructors().filter(ALL).in(TestObject.class)
            .size()
    );
    assertEquals(
        0,
        reflect().constructors().filter(NONE).in(TestObject.class).size()
    );
    assertEquals(
        0,
        reflect().visible().constructors().filter(NONE).in(TestObject.class)
            .size()
    );
  }

}
