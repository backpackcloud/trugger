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

package org.atatec.trugger.test.element;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.UnreadableElementException;
import org.atatec.trugger.element.UnwritableElementException;
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.*;

/**
 *
 */
public class ObjectElementTest {

  class HandlingTestObject {

    public void setName(String name) {
      throw new RuntimeException();
    }

    public String getInfo() {
      throw new RuntimeException();
    }
  }

  @Test
  public void testHandlingExceptions() {
    Element name = element("name").in(new HandlingTestObject());
    assertFalse(name.isReadable());
    assertThrow(UnreadableElementException.class, () -> name.get());
    assertThrow(HandlingException.class, () -> name.set("a name"));

    Element info = element("info").in(new HandlingTestObject());
    assertFalse(info.isWritable());
    assertThrow(UnwritableElementException.class, () -> info.set("info"));
    assertThrow(HandlingException.class, () -> info.get());
  }

  class AnnotationPrecedenceTest {

    @Flag
    private String name;
    private String phone;
    private String address;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Flag
    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getAddress() {
      return address;
    }

    @Flag
    public void setAddress(String address) {
      this.address = address;
    }
  }

  @Test
  public void testAnnotationPrecedence() {
    Element name = element("name").in(AnnotationPrecedenceTest.class);
    assertTrue(name.isAnnotationPresent(Flag.class));

    Element phone = element("phone").in(AnnotationPrecedenceTest.class);
    assertTrue(phone.isAnnotationPresent(Flag.class));

    Element address = element("address").in(AnnotationPrecedenceTest.class);
    assertTrue(address.isAnnotationPresent(Flag.class));
  }

  class TypeTest {
    private String name;

    private String phone;

    private String address;

    public String getPhone() {
      return null;
    }

    public void setPhone(String phone) {

    }

    public void setAddress(String address) {

    }
  }

  @Test
  public void testType() {
    Element name = element("name").in(TypeTest.class);
    assertEquals(String.class, name.type());

    Element phone = element("phone").in(TypeTest.class);
    assertEquals(String.class, phone.type());

    Element address = element("address").in(TypeTest.class);
    assertEquals(String.class, address.type());
  }

}
