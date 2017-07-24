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
package tools.devnull.trugger.predicate;

import org.junit.Test;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.reflection.ClassPredicates;
import tools.devnull.trugger.reflection.ReflectionPredicates;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.TruggerTest.assertMatch;
import static tools.devnull.trugger.TruggerTest.assertNotMatch;
import static tools.devnull.trugger.reflection.MethodPredicates.*;
import static tools.devnull.trugger.reflection.ReflectionPredicates.annotated;

/**
 * A class for testing the reflection predicates.
 *
 * @author Marcelo "Ataxexe" Guimarães
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
    assertTrue(annotated().test(field));
    assertMatch(field, ReflectionPredicates.declared(Modifier.STATIC));

    field = getClass().getDeclaredField("defaultField");
    assertFalse(annotated().test(field));
    assertMatch(field, ReflectionPredicates.declared(Modifier.FINAL).negate());
    assertMatch(field, ReflectionPredicates.declared(Modifier.STATIC).negate());

    field = getClass().getDeclaredField("protectedField");
    assertMatch(field, ReflectionPredicates.declared(Modifier.STATIC));

    field = getClass().getDeclaredField("publicField");
    assertMatch(field, ReflectionPredicates.declared(Modifier.FINAL));
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
    assertMatch(PrivateClass.class, ClassPredicates.declared(Modifier.FINAL));
    assertMatch(PrivateClass.class, ClassPredicates.declared(Modifier.STATIC)
        .negate());

    assertMatch(DefaultClass.class, ClassPredicates.declared(Modifier.FINAL)
        .negate());
    assertMatch(DefaultClass.class, ClassPredicates.declared(Modifier.STATIC));

    assertMatch(ProtectedClass.class, ClassPredicates.declared(Modifier.FINAL)
        .negate());
    assertMatch(ProtectedClass.class, ClassPredicates.declared(Modifier.STATIC)
        .negate());

    assertMatch(PublicClass.class, ClassPredicates.declared(Modifier.FINAL)
        .negate());
    assertMatch(PublicClass.class, ClassPredicates.declared(Modifier.STATIC)
        .negate());
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

    assertMatch(c.getMethod("getValue"), getter());
    assertMatch(c.getMethod("getValue"), getterOf("value"));
    assertNotMatch(c.getMethod("getValue"), getterOf("getValue"));

    assertMatch(c.getMethod("isValue"), getter());
    assertMatch(c.getMethod("isValue"), getterOf("value"));
    assertNotMatch(c.getMethod("isValue"), getterOf("Value"));

    assertMatch(c.getMethod("value"), getter());
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

    assertNotMatch(c.getDeclaredMethod("getValue"), getter());
    assertNotMatch(c.getDeclaredMethod("getValue"), getterOf("value"));

    assertNotMatch(c.getDeclaredMethod("isValue"), getter());
    assertNotMatch(c.getDeclaredMethod("isValue"), getterOf("value"));

    assertNotMatch(c.getDeclaredMethod("value"), getter());
    assertNotMatch(c.getDeclaredMethod("value"), getterOf("value"));

    assertNotMatch(c.getMethod("getSize", int.class), getter());
    assertNotMatch(c.getMethod("getSize", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("isSize", int.class), getter());
    assertNotMatch(c.getMethod("isSize", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("size", int.class), getter());
    assertNotMatch(c.getMethod("size", int.class), getterOf("size"));

    assertNotMatch(c.getMethod("get"), getter());
    assertNotMatch(c.getMethod("is"), getter());
  }

  class GoodSetterTest {
    public void setValue(int i) {
    }
  }

  @Test
  public void testGoodSetter() throws NoSuchMethodException {
    Class c = GoodSetterTest.class;

    assertMatch(c.getMethod("setValue", int.class), setter());
    assertMatch(c.getMethod("setValue", int.class), setterOf("value"));
    assertNotMatch(c.getMethod("setValue", int.class), setterOf("setValue"));
    assertNotMatch(c.getMethod("setValue", int.class), setterOf("Value"));
  }

  class BadSetterTest {
    protected void setValue(int i) {
    }

    private void setValue(long i) {
    }

    void setValue(double i) {
    }

    public void setValue(int i, int j) {
    }

    public void value(int i) {
    }

    public int setValue(boolean b) {
      return 0;
    }

    public void set(int i) {
    }
  }

  @Test
  public void testBadSetter() throws NoSuchMethodException {
    Class c = BadSetterTest.class;

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setter());
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", long.class), setter());
    assertNotMatch(c.getDeclaredMethod("setValue", long.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", double.class), setter());
    assertNotMatch(c.getDeclaredMethod("setValue", double.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), setter());
    assertNotMatch(c.getDeclaredMethod("setValue", boolean.class), setterOf("value"));

    assertNotMatch(c.getDeclaredMethod("set", int.class), setter());

    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setter());
    assertNotMatch(c.getDeclaredMethod("setValue", int.class), setterOf("value"));
  }

}
