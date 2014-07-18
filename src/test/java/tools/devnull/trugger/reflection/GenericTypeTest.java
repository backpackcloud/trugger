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
package tools.devnull.trugger.reflection;

import org.junit.Test;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.selector.MethodSelector;
import tools.devnull.trugger.selector.MethodsSelector;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class GenericTypeTest {

  static interface TestInterface<E, T, V> {
    V doIt(T t, E v);
  }

  static class TestObject implements TestInterface<String, Class, Boolean> {
    @Flag
    public Boolean doIt(Class t, String v) {
      return null;
    }
  }

  @Test
  public void genericTypeTest() {
    assertEquals(String.class, reflect().genericType("E").in(TestObject.class));
    assertEquals(Class.class, reflect().genericType("T").in(TestObject.class));
    assertEquals(Boolean.class, reflect().genericType("V").in(TestObject.class));
  }

  @Test
  public void bridgedMethodTest() {
    MethodSelector method = reflect().method("doIt");
    Method bridgedMethod = method.withParameters(Class.class, String.class).in(TestObject.class);

    assertNotNull(method);
    assertNotNull(bridgedMethod);
    assertFalse(bridgedMethod.isBridge());
    assertTrue(bridgedMethod.isAnnotationPresent(Flag.class));

    Method bridgeMethod = method.withParameters(Object.class, Object.class).in(TestObject.class);
    assertNotNull(bridgeMethod);
    assertTrue(bridgeMethod.isBridge());

    assertEquals(bridgedMethod, reflect().bridgedMethodFor(bridgeMethod));
  }

  @Test
  public void interfaceReflectionTest() {
    List<Class> interfaces = reflect().interfaces().in(MethodsSelector.class);
    assertEquals(3, interfaces.size());
  }
}
