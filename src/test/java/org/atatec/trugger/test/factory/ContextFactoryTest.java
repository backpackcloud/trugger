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

package org.atatec.trugger.test.factory;

import org.atatec.trugger.test.Flag;
import org.atatec.trugger.util.factory.ContextFactory;
import org.atatec.trugger.util.factory.CreateException;
import org.junit.Test;

import static org.atatec.trugger.reflection.ParameterPredicates.annotatedWith;
import static org.atatec.trugger.reflection.ParameterPredicates.name;
import static org.atatec.trugger.reflection.ParameterPredicates.type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ContextFactoryTest {

  public static class TestObject {

    private final String string;
    private final int integer;

    public TestObject(String string, @Flag int integer) {
      this.string = string;
      this.integer = integer;
    }

  }

  @Test
  public void testTypeContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .put("none", type(CharSequence.class))
        .put("a string", type(String.class))
        .put(15, type(Integer.class))
        .put(() -> 10, type(int.class));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test
  public void testNamedContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        // problems to build with "-parameters" in gradle
        // problems to coverage in idea using "-parameters"
        .put("a string", name("arg0"))
        .put(10, name("arg1"));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test
  public void testAnnotationContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .put("a string", annotatedWith(Flag.class).negate())
        .put(10, annotatedWith(Flag.class));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test(expected = CreateException.class)
  public void testInsufficientContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .put("a string", type(Integer.class))
        .put(10, annotatedWith(Flag.class));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

}
