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

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo "Ataxexe" Guimarães
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
        reflect().method("foo").withoutParameters().from(TestObject.class).result()
    );
    assertNotNull(
        reflect().method("bar").from(TestObject.class).result()
    );
  }

  @Test
  public void testRecursivelySelector() {
    Object obj = new Object() {
    }; // anonymous class
    assertNull(reflect().method("toString").from(obj).result());
    assertNotNull(reflect().method("toString").deep().from(obj).result());
  }

  @Test
  public void testPredicateSelector() {
    assertNotNull(
        reflect().method("toString").filter(el -> true).from(Object.class).result()
    );
    assertNull(
        reflect().method("toString").filter(el -> false).from(Object.class).result()
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
        .from(obj)
        .result();
    assertNotNull(method);
    assertArrayEquals(new Class[]{boolean.class}, method.getParameterTypes());
    method = reflect().method("foo2")
        .withParameters(Boolean.class)
        .from(obj)
        .result();
    assertNotNull(method);
    assertArrayEquals(new Class[]{Boolean.class}, method.getParameterTypes());

    assertNull(
        reflect().method("foo2")
            .withParameters(boolean.class)
            .from(obj)
            .result()
    );

    method = reflect().method("bar")
        .withParameters(boolean.class, boolean.class)
        .from(obj)
        .result();
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
    Method method1 = reflect().method("toString").from(Object.class).result();
    Method method2 = reflect().method("toString").from(o).result();

    assertNotNull(method1);
    assertNotNull(method2);

    assertFalse(method1.equals(method2));
  }

}
