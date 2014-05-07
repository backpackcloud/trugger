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

import org.atatec.trugger.util.mock.AnnotationMock;
import org.atatec.trugger.validation.*;
import org.atatec.trugger.validation.validator.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;

import static org.atatec.trugger.util.mock.Mock.annotation;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.junit.Assert.*;

/**
 *
 */
public class ValidationTest {

  @NotNull
  @Valid
  @ValidatorClass(DomainValidator.class)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface ValidAndNotNull {

  }

  //no getters and setters...keeping it simple

  public static class Product {

    @NotNull
    public String description;

    public double price;

  }

  public static class Item {

    @ValidAndNotNull
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
    @Valid
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

  Item validItem;
  Product validProduct;

  @Before
  public void initialize() {
    invalidCustomer = new Customer();
    invalidCustomer.name = "Marcelo";
    invalidCustomer.phone = "9013901031";

    invalidPurchase = new Purchase();
    invalidPurchase.customer = invalidCustomer;

    validProduct = new Product();
    validProduct.price = 25.90;
    validProduct.description = "Thing";

    validItem = new Item();
    validItem.quantity = 1;
    validItem.product = validProduct;
  }

  @Test
  public void testInvalidSimpleValidation() {
    ValidationResult result = Validation.engine().validate(invalidCustomer);
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
  public void testInvalidDeepValidation() {
    ValidationResult result = Validation.engine().validate(invalidPurchase);

    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("customer"));
    assertTrue(result.isElementInvalid("customer.address"));
    assertTrue(result.invalidElement("customer")
        .isConstraintViolated(Valid.class));
    assertTrue(result.invalidElement("customer.address")
        .isConstraintViolated(NotNull.class));
  }

  @Test
  public void testInvalidArgumentsValidator() {
    ValidationResult result = Validation.engine().validate(new Purchase());

    assertTrue(result.isElementInvalid("customer"));
    assertFalse(result.invalidElement("customer").isConstraintViolated(Valid.class));
  }

  @Test
  public void testInvalidDomainValidator() {
    Item item = new Item();
    ValidationResult result = Validation.engine().validate(item);
    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("product"));
    assertTrue(result.invalidElement("product")
        .isConstraintViolated(ValidAndNotNull.class));

    item.product = new Product();
    result = Validation.engine().validate(item);
    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("product"));
    assertTrue(result.invalidElement("product")
        .isConstraintViolated(ValidAndNotNull.class));

    assertTrue(result.isElementInvalid("product.description"));
    assertTrue(result.invalidElement("product.description")
        .isConstraintViolated(NotNull.class));
  }

  @Test
  public void testValidDomainValidator() {
    ValidationResult result = Validation.engine().validate(validItem);

    assertTrue(result.isValid());
    assertTrue(result.invalidElements().isEmpty());
  }

  private ValidatorFactory factory = Validation.factory();

  private Validator validatorFor(Class<? extends Annotation> type) {
    return factory.create(mock(annotation(type)));
  }

  @Test
  public void testNotNullValidator() {
    Validator validator = factory.create(mock(annotation(NotNull.class)));

    assertTrue(validator.isValid(""));
    assertFalse(validator.isValid(null));
  }

  @Test
  public void testValidValidator() {
    Validator validator = factory.create(mock(annotation(Valid.class)));
    assertFalse(validator.isValid(invalidCustomer));
    assertTrue(validator.isValid(validItem));
  }

  @Test
  public void testValidsValidator() {
    Validator validator = factory.create(mock(annotation(Valids.class)));

    List list = new ArrayList<>();
    list.add(validItem);
    list.add(invalidCustomer);
    assertFalse(validator.isValid(list));
    list.remove(1);
    assertTrue(validator.isValid(list));

    Object[] objects = new Object[]{validItem, invalidCustomer};
    assertFalse(validator.isValid(objects));
    objects[1] = validProduct;
    assertTrue(validator.isValid(objects));

    Map<String, Object> map = new HashMap<>();
    map.put("item", validItem);
    map.put("customer", invalidCustomer);
    assertFalse(validator.isValid(map));
    map.remove("customer");
    assertTrue(validator.isValid(map));

    assertTrue(validator.isValid(null));
  }

  @Test
  public void testNotEmptyValidator() {
    Validator validator = validatorFor(NotEmpty.class);

    assertTrue(validator.isValid(" "));  // strings are not trimmed
    assertTrue(validator.isValid("non empty"));
    assertTrue(validator.isValid(null)); // this is for NotNull to check

    assertFalse(validator.isValid(""));

    assertTrue(validator.isValid(new int[]{1, 2, 3}));
    assertFalse(validator.isValid(new int[0]));

    assertTrue(validator.isValid(Arrays.asList(0, 1, 2, 3)));
    assertFalse(validator.isValid(Collections.emptyList()));

    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    assertTrue(validator.isValid(map));
    assertFalse(validator.isValid(Collections.emptyMap()));
  }

  @Test
  public void testMaxValidator() {
    Max constraint = new AnnotationMock<Max>() {{
      map(10.0).to(annotation.value());
      map(true).to(annotation.inclusive());
    }}.createMock();
    Validator validator = factory.create(constraint);

    assertTrue(validator.isValid(10));
    assertTrue(validator.isValid(9));
    assertFalse(validator.isValid(10.001));

    constraint = new AnnotationMock<Max>() {{
      map(-10.0).to(annotation.value());
      map(false).to(annotation.inclusive());
    }}.createMock();
    validator = factory.create(constraint);

    assertFalse(validator.isValid(-10));
    assertFalse(validator.isValid(9));
    assertTrue(validator.isValid(-10.001));
    assertTrue(validator.isValid(-14));
  }

  @Test
  public void testMinValidator() {
    Min constraint = new AnnotationMock<Min>() {{
      map(10.0).to(annotation.value());
      map(true).to(annotation.inclusive());
    }}.createMock();
    Validator validator = factory.create(constraint);

    assertTrue(validator.isValid(10));
    assertFalse(validator.isValid(9));
    assertTrue(validator.isValid(10.001));

    constraint = new AnnotationMock<Min>() {{
      map(-10.0).to(annotation.value());
      map(false).to(annotation.inclusive());
    }}.createMock();
    validator = factory.create(constraint);

    assertFalse(validator.isValid(-10));
    assertTrue(validator.isValid(9));
    assertFalse(validator.isValid(-10.001));
    assertFalse(validator.isValid(-14));
  }

}
