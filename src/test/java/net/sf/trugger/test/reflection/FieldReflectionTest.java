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
package net.sf.trugger.test.reflection;

import net.sf.trugger.ValueHandler;
import net.sf.trugger.reflection.Reflector;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static net.sf.trugger.reflection.Reflection.field;
import static net.sf.trugger.reflection.Reflection.fields;
import static net.sf.trugger.reflection.Reflection.handle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * A class for testing field reflection by the {@link Reflector}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldReflectionTest {

  private String a;
  private String b;
  private String c;

  @Before
  public void initialize() {
    a = null;
    b = null;
    c = null;
  }

  @Test
  public void testHandler() throws Exception {
    ValueHandler handler = handle(field("a")).in(this);
    assertNull(handler.value());
    handler.value("string");
    assertEquals("string", a);
    assertEquals("string", handler.value());
  }

  @Test
  public void testHandlerForCollection() {
    ValueHandler handler = handle(fields()).in(this);
    Collection values = handler.value();
    for (Object object : values) {
      assertNull(object);
    }
    handler.value("string");
    assertEquals("string", a);
    assertEquals("string", b);
    assertEquals("string", c);
    values = handler.value();
    for (Object object : values) {
      assertEquals("string", object);
    }
  }

}
