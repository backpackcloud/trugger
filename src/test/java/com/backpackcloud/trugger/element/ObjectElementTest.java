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

import com.backpackcloud.trugger.HandlingException;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;
import com.backpackcloud.trugger.Flag;

import static io.backpackcloud.kodo.Expectation.*;
import static com.backpackcloud.trugger.element.ElementPredicates.*;
import static com.backpackcloud.trugger.element.Elements.element;

/**
 *
 */
public class ObjectElementTest implements ElementExpectations {

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
    Spec.given(element("name").from(new HandlingTestObject()).get())
        .expect(it(), to().not().be(readable()))
        .expect(getValue(), to().raise(UnreadableElementException.class))
        .expect(attempToChangeValue(), to().raise(HandlingException.class));

    Spec.given(element("info").from(new HandlingTestObject()).get())
        .expect(it(), to().not().be(writable()))
        .expect(attempToChangeValue(), to().raise(UnwritableElementException.class))
        .expect(attempToChangeValue(), to().raise(HandlingException.class));
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
    Spec.given(AnnotationPrecedenceTest.class)
        .expect(elementNamed("name"), to().be(annotatedWith(Flag.class)))
        .expect(elementNamed("phone"), to().be(annotatedWith(Flag.class)))
        .expect(elementNamed("address"), to().be(annotatedWith(Flag.class)));
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
    Spec.given(TypeTest.class)
        .expect(elementNamed("name"), to().be(ofType(String.class)))
        .expect(elementNamed("phone"), to().be(ofType(String.class)))
        .expect(elementNamed("address"), to().be(ofType(String.class)));
  }

}
