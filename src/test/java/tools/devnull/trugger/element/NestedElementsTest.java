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
import tools.devnull.kodo.TestScenario;

import java.util.Date;
import java.util.Properties;

import static tools.devnull.kodo.Spec.*;
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
    TestScenario.given(element("customer.phone").in(Ticket.class))
        .it(should(notBe(NULL)))
        .it(should(be(readable())))
        .it(should(be(writable())))
        .it(should(notBe(specific())));

    TestScenario.given(element("customer.credential").in(Ticket.class))
        .it(should(be(NULL)));

    TestScenario.given(element("customer.info").in(new Ticket()))
        .it(should(notBe(NULL)))
        .it(should(notBe(readable())))
        .it(should(be(writable())))
        .it(should(notBe(specific())));

    TestScenario.given(element("customer.info.properties").in(new Ticket()))
        .it(should(notBe(NULL)))
        .it(should(notBe(readable())))
        .it(should(notBe(writable())))
        .it(should(notBe(specific())));
  }

  @Test
  public void testElementHandling() {
    Ticket ticket = new Ticket();
    ticket.customer = new Customer();
    ticket.customer.address = new Address();

    TestScenario.given(element("customer.address.line").in(ticket))
        .it(should(be(readable())))
        .it(should(be(writable())))
        .it(should(be(specific())))
        .the(value(), should(be(NULL)))

        .when(valueIsSetTo("Address line"))
        .the(value(), should(be("Address line")));
  }

}
