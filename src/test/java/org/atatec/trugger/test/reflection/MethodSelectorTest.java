/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.test.reflection;

import org.atatec.trugger.test.Flag;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MethodSelectorTest {

  static class TestObject {
    @Flag
    void foo() {
    }

    void bar() {
    }
  }

  @Test
  public void testNoSelector() {
    assertNotNull(
        reflect().method("foo").in(TestObject.class)
    );
    assertNotNull(
        reflect().method("bar").in(TestObject.class)
    );
  }

  @Test
  public void testRecursivelySelector() {
    Object obj = new Object() {
    }; // anonymous class
    assertNull(reflect().method("toString").in(obj));
    assertNotNull(reflect().method("toString").deep().in(obj));
  }

  @Test
  public void testPredicateSelector() {
    assertNotNull(
        reflect().method("toString").filter(el -> true).in(Object.class)
    );
    assertNull(
        reflect().method("toString").filter(el -> false).in(Object.class)
    );
  }

  @Test
  public void testParameterSelector() throws Exception {
    Object obj = new Object() {
      void foo(boolean b) {
      }

      void foo2(Boolean b) {
      }

      void bar(boolean b, boolean bool) {
      }
    };

    Method method = reflect().method("foo")
        .withParameters(boolean.class)
        .in(obj);
    assertNotNull(method);
    assertArrayEquals(new Class[]{boolean.class}, method.getParameterTypes());
    method = reflect().method("foo2")
        .withParameters(Boolean.class)
        .in(obj);
    assertNotNull(method);
    assertArrayEquals(new Class[]{Boolean.class}, method.getParameterTypes());

    assertNull(
        reflect().method("foo2")
            .withParameters(boolean.class)
            .in(obj)
    );

    method = reflect().method("bar")
        .withParameters(boolean.class, boolean.class)
        .in(obj);
    assertNotNull(method);
    assertArrayEquals(new Class[]{boolean.class, boolean.class}, method.getParameterTypes());
  }

  @Test
  public void precedenceTest() {
    Object o = new Object() {
      public String toString() {
        return super.toString();
      }

      ;
    };
    Method method1 = reflect().method("toString").in(Object.class);
    Method method2 = reflect().method("toString").in(o);

    assertNotNull(method1);
    assertNotNull(method2);

    assertFalse(method1.equals(method2));
  }

}
