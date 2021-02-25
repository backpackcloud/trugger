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
package com.backpackcloud.trugger.reflection;

import org.junit.Test;
import com.backpackcloud.trugger.Flag;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * @author Marcelo Guimaraes
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
        Reflection.reflect().constructors().from(TestObject.class).size()
    );
    assertEquals(
        1,
        Reflection.reflect().visible().constructors().from(TestObject.class).size()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertEquals(
        2,
        Reflection.reflect().constructors().filter(ALL).from(TestObject.class).size()
    );
    assertEquals(
        1,
        Reflection.reflect().visible().constructors().filter(ALL).from(TestObject.class)
            .size()
    );
    assertEquals(
        0,
        Reflection.reflect().constructors().filter(NONE).from(TestObject.class).size()
    );
    assertEquals(
        0,
        Reflection.reflect().visible().constructors().filter(NONE).from(TestObject.class)
            .size()
    );
  }

}
