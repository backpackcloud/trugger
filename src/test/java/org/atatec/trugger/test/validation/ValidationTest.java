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

package org.atatec.trugger.test.validation;

import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationResult;
import org.atatec.trugger.validation.validator.NotNull;
import org.atatec.trugger.validation.validator.Valid;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ValidationTest {

  //no getters and setters...keeping it simple

  public static class Product {

    @NotNull
    public String description;

    public double price;

  }

  public static class Item {

    @NotNull
    public Product product;

    public int quantity;

    public double total() {
      return product.price * quantity;
    }

  }

  public static class Purchase {

    @NotNull
    public String number;

    @NotNull
    public List<Item> items = new ArrayList<>();

    @Valid
    @NotNull
    public Customer customer;

  }

  public static class Customer {

    @NotNull
    public String name;

    @NotNull
    public String address;

    @NotNull
    public String phone;

    @NotNull
    public String email;

  }

  Customer invalidCustomer;
  Purchase invalidPurchase;

  @Before
  public void initialize() {
    invalidCustomer = new Customer();
    invalidCustomer.name = "Marcelo";
    invalidCustomer.phone = "9013901031";

    invalidPurchase = new Purchase();
    invalidPurchase.customer = invalidCustomer;
  }

  @Test
  public void testSimpleValidation() {
    ValidationResult result = new Validation().validate(invalidCustomer);
    assertTrue(result.isInvalid());
    assertFalse(result.isValid());

    assertTrue(result.isElementInvalid("email"));
    assertTrue(result.isElementInvalid("address"));

    assertFalse(result.isElementInvalid("name"));
    assertFalse(result.isElementInvalid("phone"));

    assertEquals(2, result.invalidElements().size());

    InvalidElement invalidElement = result.invalidElement("address");
    assertNotNull(invalidElement);
    assertEquals(null, invalidElement.invalidValue());
    invalidCustomer.address = "New address set";
    assertEquals(null, invalidElement.invalidValue());
    assertTrue(invalidElement.isConstraintViolated(NotNull.class));
  }

  @Test
  public void testDeepValidation() {
    ValidationResult result = new Validation().validate(invalidPurchase);

    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("customer"));
    assertTrue(result.isElementInvalid("customer.address"));
    assertTrue(result.invalidElement("customer")
        .isConstraintViolated(Valid.class));
    assertTrue(result.invalidElement("customer.address")
        .isConstraintViolated(NotNull.class));
  }

}
