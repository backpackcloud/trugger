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

import org.junit.Before;
import org.junit.Test;
import tools.devnull.trugger.Flag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.reflection.FieldPredicates.annotated;
import static tools.devnull.trugger.reflection.FieldPredicates.annotatedWith;
import static tools.devnull.trugger.reflection.FieldPredicates.assignableTo;
import static tools.devnull.trugger.reflection.FieldPredicates.type;
import static tools.devnull.trugger.reflection.Reflection.getValue;
import static tools.devnull.trugger.reflection.Reflection.reflect;
import static tools.devnull.trugger.reflection.Reflection.setValue;

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
    reflect().field("a").in(this).and(setValue("string"));
    assertEquals("string", a);
    assertEquals("string", reflect().field("a").in(this).and(getValue()));
  }

  @Test
  public void testPredicates() {
    assertTrue(
        type(String.class).test(
            reflect().field("a").in(this).result()
        )
    );
    assertTrue(
        assignableTo(String.class).test(
            reflect().field("b").in(this).result()
        )
    );
    assertTrue(
        assignableTo(CharSequence.class).test(
            reflect().field("b").in(this).result()
        )
    );
    assertFalse(
        assignableTo(String.class).test(
            reflect().field("x").in(this).result()
        )
    );
    assertTrue(
        type(int.class).test(
            reflect().field("x").in(this).result()
        )
    );
    assertTrue(
        type(Integer.class).test(
            reflect().field("z").in(this).result()
        )
    );
    assertFalse(
        type(Integer.class).test(
            reflect().field("y").in(this).result()
        )
    );
    assertTrue(
        annotatedWith(Flag.class).test(
            reflect().field("a").in(this).result()
        )
    );
    assertTrue(
        annotated().test(
            reflect().field("a").in(this).result()
        )
    );
    assertFalse(
        annotatedWith(Flag.class).test(
            reflect().field("b").in(this).result()
        )
    );
    assertFalse(
        annotated().test(
            reflect().field("b").in(this).result()
        )
    );
  }

}
