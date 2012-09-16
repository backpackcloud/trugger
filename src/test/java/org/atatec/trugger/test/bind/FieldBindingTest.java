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
package org.atatec.trugger.test.bind;

import org.junit.Test;

import static org.atatec.trugger.bind.Bind.binds;
import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.reflection.Reflection.fields;
import static org.junit.Assert.assertEquals;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldBindingTest {

  public class FieldTest {

    private int intField;
    private double doubleField;
    private boolean booleanField;
    private String stringField;

  }

  @Test
  public void testFieldBinding() {
    FieldTest object = new FieldTest();

    binds()
      .bind(10).to(fields().ofType(int.class))
      .bind(true).to(field("booleanField"))
      .bind(50.2).to(field().ofType(double.class))
      .bind("stringField").to(fields().ofType(String.class))
      .applyIn(object);

    assertEquals(10, object.intField);
    assertEquals(true, object.booleanField);
    assertEquals(50.2, object.doubleField, 1e-4);
    assertEquals("stringField", object.stringField);

    object = new FieldTest();
    binds()
      .use(new ResultResolver(10)).in(field().ofType(int.class))
      .use(new ResultResolver(true)).in(field("booleanField").ofType(boolean.class))
      .use(new ResultResolver(50.2)).in(field().ofType(double.class))
      .use(new ResultResolver("stringField")).in(fields().ofType(String.class))

      .applyIn(object);

    assertEquals(10, object.intField);
    assertEquals(true, object.booleanField);
    assertEquals(50.2, object.doubleField, 1e-4);
    assertEquals("stringField", object.stringField);
  }

}
