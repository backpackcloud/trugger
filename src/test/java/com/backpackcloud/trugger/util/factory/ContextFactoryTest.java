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

package com.backpackcloud.trugger.util.factory;

import org.junit.Test;
import com.backpackcloud.trugger.Flag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.backpackcloud.trugger.reflection.ParameterPredicates.annotatedWith;
import static com.backpackcloud.trugger.reflection.ParameterPredicates.ofName;
import static com.backpackcloud.trugger.reflection.ParameterPredicates.ofType;

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
        .use("none").when(ofType(CharSequence.class))
        .use("a string").when(ofType(String.class))
        .use(15).when(ofType(Integer.class))
        .use(() -> 10).when(ofType(int.class));
    TestObject obj = factory.create(TestObject.class).get();
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test
  public void testNamedContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .use("a string").when(ofName("string"))
        .use(0).when(ofName("integer"));
    TestObject obj = factory.create(TestObject.class).orElse(null);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(0, obj.integer);
  }

  @Test
  public void testAnnotationContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .use("a string").when(annotatedWith(Flag.class).negate())
        .use(10).when(annotatedWith(Flag.class));
    TestObject obj = factory.create(TestObject.class).orElse(null);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test
  public void testInsufficientContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .use("a string").when(ofType(Integer.class))
        .use(10).when(annotatedWith(Flag.class));
    assertNull(factory.create(TestObject.class).orElse(null));
  }

}
