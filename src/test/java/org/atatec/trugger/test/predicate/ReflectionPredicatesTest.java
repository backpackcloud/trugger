/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.test.predicate;

import org.atatec.trugger.reflection.ClassPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.atatec.trugger.reflection.ReflectionPredicates.ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.NOT_ANNOTATED;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class for testing the reflection predicates.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ReflectionPredicatesTest {

  @Flag
  private static Object privateField = null;
  Object defaultField = null;
  protected static Object protectedField = null;
  public final Object publicField = null;

  @Test
  public void memberPredicatesTest() throws Exception {
    Field field = getClass().getDeclaredField("privateField");
    assertTrue(ANNOTATED.evaluate(field));
    assertFalse(NOT_ANNOTATED.evaluate(field));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.declare(Modifier.STATIC));

    field = getClass().getDeclaredField("defaultField");
    assertFalse(ANNOTATED.evaluate(field));
    assertTrue(NOT_ANNOTATED.evaluate(field));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.STATIC));

    field = getClass().getDeclaredField("protectedField");
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.declare(Modifier.STATIC));

    field = getClass().getDeclaredField("publicField");
    assertMatch(field, ReflectionPredicates.declare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.STATIC));
  }

  private final class PrivateClass {}

  static class DefaultClass {}

  protected class ProtectedClass {}

  public class PublicClass {}

  @Test
  public void classPredicatesTest() throws Exception {
    assertMatch(PrivateClass.class, ClassPredicates.declare(Modifier.FINAL));
    assertMatch(PrivateClass.class, ClassPredicates.dontDeclare(Modifier.STATIC));

    assertMatch(DefaultClass.class, ClassPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(DefaultClass.class, ClassPredicates.declare(Modifier.STATIC));

    assertMatch(ProtectedClass.class, ClassPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(ProtectedClass.class, ClassPredicates.dontDeclare(Modifier.STATIC));

    assertMatch(PublicClass.class, ClassPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(PublicClass.class, ClassPredicates.dontDeclare(Modifier.STATIC));
  }

}
