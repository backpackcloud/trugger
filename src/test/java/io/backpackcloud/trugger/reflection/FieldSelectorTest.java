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

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Marcelo Guimaraes
 */
public class FieldSelectorTest {

  static class BaseClassTest {
    int i;
  }

  static class RecursivelySelectorTest extends BaseClassTest {

  }

  @Test
  public void testRecursivelySelector() {
    assertNull(Reflection.reflect()
        .field("i")
        .from(RecursivelySelectorTest.class)
        .orElse(null)
    );
    assertNotNull(Reflection.reflect()
        .field("i")
        .deep()
        .from(RecursivelySelectorTest.class)
        .orElse(null)
    );
  }

  @Test
  public void testPredicateSelector() {
    assertNull(
        Reflection.reflect().field("i")
            .deep()
            .filter(f -> false)
            .from(RecursivelySelectorTest.class)
            .orElse(null)
    );
  }

  static class PrecedenceTest extends BaseClassTest {
    int i;
  }

  @Test
  public void testPrecedence() {
    FieldSelector reflector = Reflection.reflect().field("i");
    Field field1 = reflector.from(BaseClassTest.class).map(ReflectedField::actualField).orElse(null);
    Field field2 = reflector.deep().from(PrecedenceTest.class).map(ReflectedField::actualField).orElse(null);

    assertNotNull(field1);
    assertNotNull(field2);
    assertNotEquals(field1, field2);
  }

}
