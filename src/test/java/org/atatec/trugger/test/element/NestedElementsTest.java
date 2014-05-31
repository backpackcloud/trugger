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

import org.atatec.trugger.element.Element;
import org.junit.Test;

import java.util.Date;
import java.util.Properties;

import static org.atatec.trugger.element.Elements.element;
import static org.junit.Assert.*;

public class NestedElementsTest {

  class Ticket {
    private Customer customer;
    private Date arrival;
  }

  class Customer {
    private Address address;
    private String phone;

    public void setInfo(Info info) {

    }

    //not a setter method
    private void setCredential(String arg) {

    }
  }

  class Address {
    private String line;
    private Info info;
  }

  class Info {
    private Properties properties;
  }

  @Test
  public void testNestedElementCreating() {
    Element phone = element("customer.phone").in(Ticket.class);
    assertNotNull(phone);
    assertTrue(phone.isReadable());
    assertTrue(phone.isWritable());
    assertFalse(phone.isSpecific());

    Element element = element("customer.credential").in(Ticket.class);
    assertNull(element);

    Element info = element("customer.info").in(new Ticket());
    assertNotNull(info);
    assertFalse(info.isReadable());
    assertTrue(info.isWritable());
    assertFalse(info.isSpecific());

    Element properties = element("customer.info.properties").in(new Ticket());
    assertNotNull(properties);
    assertFalse(properties.isReadable());
    assertFalse(properties.isWritable());
    assertFalse(properties.isSpecific());
  }

  @Test
  public void testElementHandling() {
    Ticket ticket = new Ticket();
    ticket.customer = new Customer();
    ticket.customer.address = new Address();

    Element line = element("customer.address.line").in(ticket);
    assertTrue(line.isReadable());
    assertTrue(line.isWritable());
    assertTrue(line.isSpecific());
    assertNull(line.value());

    line.set("Address line");

    assertEquals("Address line", line.value());
  }

}
