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

package tools.devnull.trugger.factory;

import org.junit.Test;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.util.factory.ContextFactory;
import tools.devnull.trugger.util.factory.CreateException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static tools.devnull.trugger.reflection.ParameterPredicates.annotatedWith;
import static tools.devnull.trugger.reflection.ParameterPredicates.named;
import static tools.devnull.trugger.reflection.ParameterPredicates.type;

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
        .use("none").when(type(CharSequence.class))
        .use("a string").when(type(String.class))
        .use(15).when(type(Integer.class))
        .use(() -> 10).when(type(int.class));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test
  public void testNamedContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .use("a string").when(named("string"))
        .use(0).when(named("integer"));
    TestObject obj = factory.create(TestObject.class);
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
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

  @Test(expected = CreateException.class)
  public void testInsufficientContext() {
    ContextFactory factory = new ContextFactory();
    factory.context()
        .use("a string").when(type(Integer.class))
        .use(10).when(annotatedWith(Flag.class));
    TestObject obj = factory.create(TestObject.class);
    assertNotNull(obj);
    assertEquals("a string", obj.string);
    assertEquals(10, obj.integer);
  }

}
