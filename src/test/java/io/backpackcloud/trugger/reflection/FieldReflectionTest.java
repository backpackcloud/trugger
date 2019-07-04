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

import org.junit.Before;
import org.junit.Test;
import io.backpackcloud.trugger.Flag;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static io.backpackcloud.trugger.reflection.FieldPredicates.annotated;
import static io.backpackcloud.trugger.reflection.FieldPredicates.annotatedWith;
import static io.backpackcloud.trugger.reflection.FieldPredicates.assignableTo;
import static io.backpackcloud.trugger.reflection.FieldPredicates.ofType;

/**
 * A class for testing field reflection by the {@link Reflector}.
 *
 * @author Marcelo Guimaraes
 */
public class FieldReflectionTest {

  @Flag
  private String a;
  private String b;
  private String c;

  @Flag
  private int x;
  private int y;
  private Integer z;

  @Before
  public void initialize() {
    a = null;
    b = null;
    c = null;
  }

  @Test
  public void testHandler() {
    Field field = Reflection.reflect().field("a").from(this).map(ReflectedField::unwrap).get();
    Reflection.handle(field).on(this).setValue("string");
    assertEquals("string", this.a);
    assertEquals("string", Reflection.handle(field).on(this).getValue());
  }

  @Test
  public void testPredicates() {
    assertTrue(
        ofType(String.class).test(
            Reflection.reflect().field("a").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        assignableTo(String.class).test(
            Reflection.reflect().field("b").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        assignableTo(CharSequence.class).test(
            Reflection.reflect().field("b").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertFalse(
        assignableTo(String.class).test(
            Reflection.reflect().field("x").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        ofType(int.class).test(
            Reflection.reflect().field("x").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        ofType(Integer.class).test(
            Reflection.reflect().field("z").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertFalse(
        ofType(Integer.class).test(
            Reflection.reflect().field("y").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        annotatedWith(Flag.class).test(
            Reflection.reflect().field("a").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertTrue(
        annotated().test(
            Reflection.reflect().field("a").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertFalse(
        annotatedWith(Flag.class).test(
            Reflection.reflect().field("b").from(this).map(ReflectedField::unwrap).get()
        )
    );
    assertFalse(
        annotated().test(
            Reflection.reflect().field("b").from(this).map(ReflectedField::unwrap).get()
        )
    );
  }

}
