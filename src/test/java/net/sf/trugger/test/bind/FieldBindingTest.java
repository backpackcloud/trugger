/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.bind;

import static net.sf.trugger.bind.Bind.newBinder;
import static org.junit.Assert.assertEquals;
import net.sf.trugger.Resolver;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldBindingTest {

  public class FieldTest {

    private int intField;
    private boolean booleanField;
    private String stringField;

  }

  @Test
  public void testFieldBinding() {
    Binder binder = newBinder();
    binder.bind(10).toFields().ofType(int.class);
    binder.bind(true).toField("booleanField");
    binder.use(new Resolver<Object, Element>() {
      public Object resolve(Element target) {
        return target.name();
      }
    }).toFields().ofType(String.class);

    FieldTest object = new FieldTest();

    binder.applyBinds(object);

    assertEquals(10, object.intField);
    assertEquals(true, object.booleanField);
    assertEquals("stringField", object.stringField);
  }

}
