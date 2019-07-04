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

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Marcelo Guimaraes
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
        Reflection.reflect().fields().from(TestObject.class).size()
    );
    assertEquals(
        0,
        Reflection.reflect().fields().from(this).size()
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
        Reflection.reflect().fields().from(ExtendedClassTest.class).isEmpty()
    );
    assertFalse(
        Reflection.reflect().fields().deep().from(ExtendedClassTest.class).isEmpty()
    );
  }

  @Test
  public void testPredicateSelector() {
    assertFalse(
        Reflection.reflect().fields()
            .filter(el -> true)
            .from(TestObject.class)
            .isEmpty()
    );
    assertTrue(
        Reflection.reflect().fields()
            .filter(el -> false)
            .from(TestObject.class)
            .isEmpty()
    );
  }

}
