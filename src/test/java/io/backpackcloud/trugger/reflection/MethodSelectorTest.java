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

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author Marcelo Guimaraes
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
        Reflection.reflect()
            .method("foo")
            .withoutParameters()
            .from(TestObject.class)
            .orElse(null)
    );
    assertNotNull(
        Reflection.reflect()
            .method("bar")
            .from(TestObject.class)
            .orElse(null)
    );
  }

  @Test
  public void testRecursivelySelector() {
    Object obj = new Object() {
    }; // anonymous class
    assertNull(Reflection.reflect()
        .method("toString")
        .from(obj)
        .orElse(null)
    );
    assertNotNull(Reflection.reflect()
        .method("toString")
        .deep()
        .from(obj)
        .orElse(null)
    );
  }

  @Test
  public void testPredicateSelector() {
    assertNotNull(
        Reflection.reflect()
            .method("toString")
            .filter(el -> true)
            .from(Object.class)
            .orElse(null)
    );
    assertNull(
        Reflection.reflect()
            .method("toString")
            .filter(el -> false)
            .from(Object.class)
            .orElse(null)
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

    Method method = Reflection.reflect().method("foo")
        .withParameters(boolean.class)
        .from(obj)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);

    assertNotNull(method);
    assertArrayEquals(new Class[]{boolean.class}, method.getParameterTypes());
    method = Reflection.reflect().method("foo2")
        .withParameters(Boolean.class)
        .from(obj)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);

    assertNotNull(method);
    assertArrayEquals(new Class[]{Boolean.class}, method.getParameterTypes());

    assertNull(
        Reflection.reflect().method("foo2")
            .withParameters(boolean.class)
            .from(obj)
            .orElse(null)
    );

    method = Reflection.reflect().method("bar")
        .withParameters(boolean.class, boolean.class)
        .from(obj)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);

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
    Method method1 = Reflection.reflect()
        .method("toString")
        .from(Object.class)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);

    Method method2 = Reflection.reflect()
        .method("toString")
        .from(o)
        .map(ReflectedMethod::actualMethod)
        .orElse(null);

    assertNotNull(method1);
    assertNotNull(method2);

    assertNotEquals(method1, method2);
  }

}
