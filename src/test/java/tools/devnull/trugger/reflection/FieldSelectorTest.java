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

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class FieldSelectorTest {

  static class BaseClassTest {
    int i;
  }

  static class RecursivelySelectorTest extends BaseClassTest {

  }

  @Test
  public void testRecursivelySelector() {
    assertNull(reflect().field("i").from(RecursivelySelectorTest.class).result());
    assertNotNull(reflect().field("i").deep().from(RecursivelySelectorTest.class).result());
  }

  @Test
  public void testPredicateSelector() {
    assertNull(
        reflect().field("i")
            .deep()
            .filter(f -> false)
            .from(RecursivelySelectorTest.class)
            .result()
    );
  }

  static class PrecedenceTest extends BaseClassTest {
    int i;
  }

  @Test
  public void testPrecedence() {
    FieldSelector reflector = reflect().field("i");
    Field field1 = reflector.from(BaseClassTest.class).result();
    Field field2 = reflector.deep().from(PrecedenceTest.class).result();

    assertNotNull(field1);
    assertNotNull(field2);
    assertFalse(field1.equals(field2));
  }

}
