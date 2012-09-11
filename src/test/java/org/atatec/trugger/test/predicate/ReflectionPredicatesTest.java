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

import static org.atatec.trugger.reflection.ReflectionPredicates.IS_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.IS_GETTER;
import static org.atatec.trugger.reflection.ReflectionPredicates.IS_NOT_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.IS_SETTER;
import static org.atatec.trugger.reflection.ReflectionPredicates.isGetterOf;
import static org.atatec.trugger.reflection.ReflectionPredicates.isSetterOf;
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
    assertTrue(IS_ANNOTATED.evaluate(field));
    assertFalse(IS_NOT_ANNOTATED.evaluate(field));
    assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
    assertMatch(field, ReflectionPredicates.declare(Modifier.STATIC));

    field = getClass().getDeclaredField("defaultField");
    assertFalse(IS_ANNOTATED.evaluate(field));
    assertTrue(IS_NOT_ANNOTATED.evaluate(field));
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

    assertMatch(c.getMethod("getValue"), IS_GETTER);
    assertMatch(c.getMethod("getValue"), isGetterOf("value"));
    assertNotMatch(c.getMethod("getValue"), isGetterOf("getValue"));

    assertMatch(c.getMethod("isValue"), IS_GETTER);
    assertMatch(c.getMethod("isValue"), isGetterOf("value"));
    assertNotMatch(c.getMethod("isValue"), isGetterOf("Value"));

    assertMatch(c.getMethod("value"), IS_GETTER);
    assertMatch(c.getMethod("value"), isGetterOf("value"));
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

    assertNotMatch(c.getDeclaredMethod("getValue"), IS_GETTER);
    assertNotMatch(c.getDeclaredMethod("getValue"), isGetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("isValue"), IS_GETTER);
    assertNotMatch(c.getDeclaredMethod("isValue"), isGetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("value"), IS_GETTER);
    assertNotMatch(c.getDeclaredMethod("value"), isGetterOf("value"));

    assertNotMatch(c.getMethod("getSize", int.class), IS_GETTER);
    assertNotMatch(c.getMethod("getSize", int.class), isGetterOf("size"));

    assertNotMatch(c.getMethod("isSize", int.class), IS_GETTER);
    assertNotMatch(c.getMethod("isSize", int.class), isGetterOf("size"));

    assertNotMatch(c.getMethod("size", int.class), IS_GETTER);
    assertNotMatch(c.getMethod("size", int.class), isGetterOf("size"));

    assertNotMatch(c.getMethod("get"), IS_GETTER);
    assertNotMatch(c.getMethod("is"), IS_GETTER);
  }

  class GoodSetterTest {
    public void setValue(int i) {
    }
  }

  @Test
  public void testGoodSetter() throws NoSuchMethodException {
    Class c = GoodSetterTest.class;

    assertMatch(c.getMethod("setValue", int.class), IS_SETTER);
    assertMatch(c.getMethod("setValue", int.class), isSetterOf("value"));
    assertNotMatch(c.getMethod("setValue", int.class), isSetterOf("setValue"));
    assertNotMatch(c.getMethod("setValue", int.class), isSetterOf("Value"));
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

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), IS_SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), isSetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", long.class), IS_SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", long.class), isSetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", double.class), IS_SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", double.class), isSetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), IS_SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), isSetterOf("value"));

    assertNotMatch(c.getDeclaredMethod("set", int.class), IS_SETTER);

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), IS_SETTER);
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), isSetterOf("value"));
  }

}
