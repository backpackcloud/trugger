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
import static org.atatec.trugger.reflection.ReflectionPredicates.GETTER;
import static org.atatec.trugger.reflection.ReflectionPredicates.NOT_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.SETTER;
import static org.atatec.trugger.reflection.ReflectionPredicates.getterOf;
import static org.atatec.trugger.reflection.ReflectionPredicates.setterOf;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNotMatch;
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
    assertTrue(ANNOTATED.test(field));
    assertFalse(NOT_ANNOTATED.test(field));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.declare(Modifier.STATIC));

    field = getClass().getDeclaredField("defaultField");
    assertFalse(ANNOTATED.test(field));
    assertTrue(NOT_ANNOTATED.test(field));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.STATIC));

    field = getClass().getDeclaredField("protectedField");
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.declare(Modifier.STATIC));

    field = getClass().getDeclaredField("publicField");
    assertMatch(field, ReflectionPredicates.declare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.STATIC));
  }

  private final class PrivateClass {
  }

  static class DefaultClass {
  }

  protected class ProtectedClass {
  }

  public class PublicClass {
  }

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

  class GoodGetterTest {

    public int getValue() {
      return 0;
    }

    public int value() {
      return 0;
    }

    public boolean isValue() {
      return false;
    }

  }

  @Test
  public void testGoodGetters() throws NoSuchMethodException {
    Class c = GoodGetterTest.class;

    assertMatch(c.getMethod("getValue"), GETTER);
    assertMatch(c.getMethod("getValue"), getterOf("value"));
    assertNotMatch(c.getMethod("getValue"), getterOf("getValue"));

    assertMatch(c.getMethod("isValue"), GETTER);
    assertMatch(c.getMethod("isValue"), getterOf("value"));
    assertNotMatch(c.getMethod("isValue"), getterOf("Value"));

    assertMatch(c.getMethod("value"), GETTER);
    assertMatch(c.getMethod("value"), getterOf("value"));
  }

  class BadGetterTest {

    protected int getValue() {
      return 0;
    }

    int value() {
      return 0;
    }

    private boolean isValue() {
      return false;
    }

    public int getSize(int i) {
      return 0;
    }

    public int size(int i) {
      return 0;
    }

    public boolean isSize(int i) {
      return false;
    }

    public boolean get() {
      return false;
    }

    public boolean is() {
      return true;
    }

  }

  @Test
  public void testBadGetters() throws NoSuchMethodException {
    Class c = BadGetterTest.class;

    assertNotMatch(c.getDeclaredMethod("getValue"), GETTER);
    assertNotMatch(c.getDeclaredMethod("getValue"), getterOf("value"));

    assertNotMatch(c.getDeclaredMethod("isValue"), GETTER);
    assertNotMatch(c.getDeclaredMethod("isValue"), getterOf("value"));

    assertNotMatch(c.getDeclaredMethod("value"), GETTER);
    assertNotMatch(c.getDeclaredMethod("value"), getterOf("value"));

    assertNotMatch(c.getMethod("getSize", int.class), GETTER);
    assertNotMatch(c.getMethod("getSize", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("isSize", int.class), GETTER);
    assertNotMatch(c.getMethod("isSize", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("size", int.class), GETTER);
    assertNotMatch(c.getMethod("size", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("get"), GETTER);
    assertNotMatch(c.getMethod("is"), GETTER);
  }

  class GoodSetterTest {
    public void setValue(int i) {
    }
  }

  @Test
  public void testGoodSetter() throws NoSuchMethodException {
    Class c = GoodSetterTest.class;

    assertMatch(c.getMethod("setValue", int.class), SETTER);
    assertMatch(c.getMethod("setValue", int.class), setterOf("value"));
    assertNotMatch(c.getMethod("setValue", int.class), setterOf("setValue"));
    assertNotMatch(c.getMethod("setValue", int.class), setterOf("Value"));
  }

  class BadSetterTest {
    protected void setValue(int i) {}
    private void setValue(long i) {}
    void setValue(double i) {}

    public void setValue(int i, int j) {}
    public void value(int i) {}
    public int setValue(boolean b) {
      return 0;
    }
    public void set(int i){}
  }

  @Test
  public void testBadSetter() throws NoSuchMethodException {
    Class c = BadSetterTest.class;

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", long.class), SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", long.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", double.class), SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", double.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("set", int.class), SETTER);

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setterOf("value"));
  }

}
