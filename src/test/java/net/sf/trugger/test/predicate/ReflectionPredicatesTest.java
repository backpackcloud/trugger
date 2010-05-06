/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.predicate;

import static net.sf.trugger.reflection.ReflectionPredicates.ANNOTATED;
import static net.sf.trugger.reflection.ReflectionPredicates.FINAL_CLASS;
import static net.sf.trugger.reflection.ReflectionPredicates.NON_FINAL_CLASS;
import static net.sf.trugger.reflection.ReflectionPredicates.NON_STATIC_CLASS;
import static net.sf.trugger.reflection.ReflectionPredicates.NOT_ANNOTATED;
import static net.sf.trugger.reflection.ReflectionPredicates.STATIC_CLASS;
import static net.sf.trugger.test.TruggerTest.assertAccess;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static net.sf.trugger.test.TruggerTest.assertNotAccess;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.test.Flag;

import org.junit.Test;

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
    assertAccess(field, Access.PRIVATE);
    assertNotAccess(field, Access.DEFAULT);
    assertNotAccess(field, Access.LIKE_DEFAULT);
    assertNotAccess(field, Access.PROTECTED);
    assertNotAccess(field, Access.LIKE_PROTECTED);
    assertNotAccess(field, Access.PUBLIC);
    assertMatch(field, ReflectionPredicates.NON_FINAL);
    assertMatch(field, ReflectionPredicates.STATIC);

    field = getClass().getDeclaredField("defaultField");
    assertFalse(ANNOTATED.evaluate(field));
    assertTrue(NOT_ANNOTATED.evaluate(field));
    assertNotAccess(field, Access.PRIVATE);
    assertAccess(field, Access.DEFAULT);
    assertAccess(field, Access.LIKE_DEFAULT);
    assertNotAccess(field, Access.PROTECTED);
    assertNotAccess(field, Access.LIKE_PROTECTED);
    assertNotAccess(field, Access.PUBLIC);
    assertMatch(field, ReflectionPredicates.NON_FINAL);
    assertMatch(field, ReflectionPredicates.NON_STATIC);

    field = getClass().getDeclaredField("protectedField");
    assertNotAccess(field, Access.PRIVATE);
    assertNotAccess(field, Access.DEFAULT);
    assertAccess(field, Access.LIKE_DEFAULT);
    assertAccess(field, Access.PROTECTED);
    assertAccess(field, Access.LIKE_PROTECTED);
    assertNotAccess(field, Access.PUBLIC);
    assertMatch(field, ReflectionPredicates.NON_FINAL);
    assertMatch(field, ReflectionPredicates.STATIC);

    field = getClass().getDeclaredField("publicField");
    assertNotAccess(field, Access.PRIVATE);
    assertNotAccess(field, Access.DEFAULT);
    assertAccess(field, Access.LIKE_DEFAULT);
    assertNotAccess(field, Access.PROTECTED);
    assertAccess(field, Access.LIKE_PROTECTED);
    assertAccess(field, Access.PUBLIC);
    assertMatch(field, ReflectionPredicates.FINAL);
    assertMatch(field, ReflectionPredicates.NON_STATIC);
  }

  private final class PrivateClass {}

  static class DefaultClass {}

  protected class ProtectedClass {}

  public class PublicClass {}

  @Test
  public void classPredicatesTest() throws Exception {
    assertAccess(PrivateClass.class, Access.PRIVATE);
    assertNotAccess(PrivateClass.class, Access.DEFAULT);
    assertNotAccess(PrivateClass.class, Access.LIKE_DEFAULT);
    assertNotAccess(PrivateClass.class, Access.PROTECTED);
    assertNotAccess(PrivateClass.class, Access.LIKE_PROTECTED);
    assertNotAccess(PrivateClass.class, Access.PUBLIC);
    assertMatch(PrivateClass.class, FINAL_CLASS);
    assertMatch(PrivateClass.class, NON_STATIC_CLASS);

    assertNotAccess(DefaultClass.class, Access.PRIVATE);
    assertAccess(DefaultClass.class, Access.DEFAULT);
    assertAccess(DefaultClass.class, Access.LIKE_DEFAULT);
    assertNotAccess(DefaultClass.class, Access.PROTECTED);
    assertNotAccess(DefaultClass.class, Access.LIKE_PROTECTED);
    assertNotAccess(DefaultClass.class, Access.PUBLIC);
    assertMatch(DefaultClass.class, NON_FINAL_CLASS);
    assertMatch(DefaultClass.class, STATIC_CLASS);

    assertNotAccess(ProtectedClass.class, Access.PRIVATE);
    assertNotAccess(ProtectedClass.class, Access.DEFAULT);
    assertAccess(ProtectedClass.class, Access.LIKE_DEFAULT);
    assertAccess(ProtectedClass.class, Access.PROTECTED);
    assertAccess(ProtectedClass.class, Access.LIKE_PROTECTED);
    assertNotAccess(ProtectedClass.class, Access.PUBLIC);
    assertMatch(ProtectedClass.class, NON_FINAL_CLASS);
    assertMatch(ProtectedClass.class, NON_STATIC_CLASS);

    assertNotAccess(PublicClass.class, Access.PRIVATE);
    assertNotAccess(PublicClass.class, Access.DEFAULT);
    assertAccess(PublicClass.class, Access.LIKE_DEFAULT);
    assertNotAccess(PublicClass.class, Access.PROTECTED);
    assertAccess(PublicClass.class, Access.LIKE_PROTECTED);
    assertAccess(PublicClass.class, Access.PUBLIC);
    assertMatch(PublicClass.class, NON_FINAL_CLASS);
    assertMatch(PublicClass.class, NON_STATIC_CLASS);
  }

}
