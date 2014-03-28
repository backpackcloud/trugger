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

import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.reflection.Reflector;
import org.atatec.trugger.test.Flag;
import org.junit.Before;
import org.junit.Test;

import static org.atatec.trugger.reflection.FieldPredicates.*;
import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.reflection.Reflection.handle;
import static org.junit.Assert.*;

/**
 * A class for testing field reflection by the {@link Reflector}.
 *
 * @author Marcelo Varella Barca Guimarães
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
    ValueHandler handler = handle(field("a")).in(this);
    assertNull(handler.get());
    handler.set("string");
    assertEquals("string", a);
    assertEquals("string", handler.get());
  }

  @Test
  public void testPredicates() {
    assertTrue(
        type(String.class).test(
            field("a").in(this)
        )
    );
    assertTrue(
        assignableTo(String.class).test(
            field("b").in(this)
        )
    );
    assertTrue(
        assignableTo(CharSequence.class).test(
            field("b").in(this)
        )
    );
    assertFalse(
        assignableTo(String.class).test(
            field("x").in(this)
        )
    );
    assertTrue(
        type(int.class).test(
            field("x").in(this)
        )
    );
    assertTrue(
        type(Integer.class).test(
            field("z").in(this)
        )
    );
    assertFalse(
        type(Integer.class).test(
            field("y").in(this)
        )
    );
    assertTrue(
        annotatedWith(Flag.class).test(
            field("a").in(this)
        )
    );
    assertTrue(
        annotated().test(
            field("a").in(this)
        )
    );
    assertFalse(
        annotatedWith(Flag.class).test(
            field("b").in(this)
        )
    );
    assertFalse(
        annotated().test(
            field("b").in(this)
        )
    );
  }

}
