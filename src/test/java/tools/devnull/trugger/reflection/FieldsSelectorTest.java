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

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldsSelectorTest {

  static class TestObject {
    @Flag
    @Resource
    private String annotatedField;
    private String notAnnotatedField;
    @Resource
    private String field;
  }

  @Test
  public void test() {
    assertEquals(
        3,
        reflect().fields().in(TestObject.class).size()
    );
    assertEquals(
        0,
        reflect().fields().in(this).size()
    );
  }

  static class BaseClassTest {
    int i;
  }

  static class ExtendedClassTest extends BaseClassTest {

  }

  @Test
  public void testRecursivelySelector() {
    assertTrue(
        reflect().fields().in(ExtendedClassTest.class).isEmpty()
    );
    assertFalse(
        reflect().fields().deep().in(ExtendedClassTest.class).isEmpty()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertFalse(
        reflect().fields()
            .filter(el -> true)
            .in(TestObject.class)
            .isEmpty()
    );
    assertTrue(
        reflect().fields()
            .filter(el -> false)
            .in(TestObject.class)
            .isEmpty()
    );
  }

}
