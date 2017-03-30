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

package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

import static tools.devnull.kodo.Expectation.*;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;

public class NestedElementsTest implements ElementSpecs {

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
    Spec.given(element("customer.phone").in(Ticket.class))
        .expect(it(), to().not().beNull())
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().not().be(specific()));

    Spec.given(element("customer.credential").in(Ticket.class))
        .expect(it(), to().beNull());

    Spec.given(element("customer.info").in(new Ticket()))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().not().be(specific()));

    Spec.given(element("customer.info.properties").in(new Ticket()))
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

    Spec.given(element("customer.address.line").in(ticket))
        .expect(it(), to().be(readable()))
        .expect(it(), to().be(writable()))
        .expect(it(), to().be(specific()))
        .expect((Function<? super Element, Object>) Element::value, to().equal(null))

        .when(valueIsSetTo("Address line"))
        .expect(Element::value, to().be("Address line"));
  }

}
