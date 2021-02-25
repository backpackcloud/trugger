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

package com.backpackcloud.trugger.element;

import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

import static io.backpackcloud.kodo.Expectation.*;
import static com.backpackcloud.trugger.element.ElementPredicates.*;
import static com.backpackcloud.trugger.element.Elements.element;

public class NestedElementsTest implements ElementExpectations {

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
    Spec.given(element("customer.phone").from(Ticket.class).orElse(null))
        .expect(it(), to().not().beNull())
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().not().be(specific()));

    Spec.given(element("customer.credential").from(Ticket.class).orElse(null))
        .expect(it(), to().beNull());

    Spec.given(element("customer.info").from(new Ticket()).orElse(null))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().not().be(specific()));

    Spec.given(element("customer.info.properties").from(new Ticket()).orElse(null))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(readable()))
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().not().be(specific()));
  }

  @Test
  public void testElementHandling() {
    Ticket ticket = new Ticket();
    ticket.customer = new Customer();
    ticket.customer.address = new Address();

    Spec.given(element("customer.address.line").from(ticket).orElse(null))
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().be(specific()))
        .expect((Function<? super Element, Object>) Element::getValue, to().eq(null))

        .when(valueIsSetTo("Address line"))
        .expect(Element::getValue, to().be("Address line"));
  }

}
