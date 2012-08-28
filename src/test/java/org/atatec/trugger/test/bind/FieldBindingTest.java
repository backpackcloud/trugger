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

import org.atatec.trugger.bind.Binder;
import org.junit.Test;

import static org.atatec.trugger.bind.Bind.newBinder;
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
    Binder binder = newBinder();
    binder.bind(10).toFields().ofType(int.class);
    binder.bind(true).toField("booleanField");
    binder.bind(50.2).toField().ofType(double.class);
    binder.bind("stringField").toFields().ofType(String.class);

    FieldTest object = new FieldTest();

    binder.applyBinds(object);

    assertEquals(10, object.intField);
    assertEquals(true, object.booleanField);
    assertEquals(50.2, object.doubleField, 1e-4);
    assertEquals("stringField", object.stringField);

    binder = newBinder();
    binder.use(new ResultResolver(10)).toField().ofType(int.class);
    binder.use(new ResultResolver(true)).toField("booleanField").ofType(boolean.class);
    binder.use(new ResultResolver(50.2)).toField().ofType(double.class);
    binder.use(new ResultResolver("stringField")).toFields().ofType(String.class);

    object = new FieldTest();

    binder.applyBinds(object);

    assertEquals(10, object.intField);
    assertEquals(true, object.booleanField);
    assertEquals(50.2, object.doubleField, 1e-4);
    assertEquals("stringField", object.stringField);
  }

}
