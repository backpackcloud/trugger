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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorTest {

  public static class NoDeclaredConstructor {

  }

  @Test
  public void testNotDeclaredConstructor() {
    assertNotNull(
        reflect().constructor()
            .withoutParameters()
            .in(NoDeclaredConstructor.class)
    );
    assertNotNull(
        reflect().visible().constructor()
            .withoutParameters()
            .in(NoDeclaredConstructor.class)
    );
    assertEquals(
        1,
        reflect().constructors().in(NoDeclaredConstructor.class).size()
    );
  }

  @Test
  public void testInvoker() {
    Constructor<?> constructor = reflect().constructor()
        .withoutParameters()
        .in(ArrayList.class);
    assertNotNull(constructor);
    Object object = invoke(constructor).withoutArgs();
    assertTrue(object instanceof ArrayList);
  }

}
