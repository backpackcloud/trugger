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
package net.sf.trugger.test.predicate;

import static net.sf.trugger.util.mock.Mock.element;
import static net.sf.trugger.util.mock.Mock.mock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.trugger.element.Element;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateDSL;
import net.sf.trugger.validation.validator.NotNull;

import org.junit.Test;


/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class PredicateDSLTestExcept {
  @Test
  public void interfaceTest() {
    Predicate<Element> predicate = new PredicateDSL<Element>() {{
        expect(obj.name()).equal("elementA");
      }};
    assertTrue(predicate.evaluate(mock(element().named("elementA"))));
    assertFalse(predicate.evaluate(mock(element().named("elementB"))));
  }

  static class TestObject {

    private String attr;
    private int i;

    TestObject() {}

    TestObject(String attr) {
      this.attr = attr;
    }

    TestObject(int i) {
      this.i = i;
    }

    public String attr() {
      return attr;
    }

    public String attr(String value) {
      if ("value".equals(value)) {
        return attr;
      }
      return null;
    }

    public int i() {
      return i;
    }

  }

  @Test
  public void classTest() {
    Predicate<TestObject> predicate = new PredicateDSL<TestObject>(){{
      expect(obj.attr()).equal("value");
    }};
    assertTrue(predicate.evaluate(new TestObject("value")));
    assertFalse(predicate.evaluate(new TestObject(null)));
  }

  @Test
  public void argumentTest() throws Exception {
    Predicate<TestObject> predicate = new PredicateDSL<TestObject>(){{
      expect(obj.attr("value")).equal("string");
    }};
    assertTrue(predicate.evaluate(new TestObject("string")));
    assertFalse(predicate.evaluate(new TestObject(null)));
  }

  @Test
  public void testDiffer() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.i()).differ(1);
    }};
    assertTrue(p.evaluate(new TestObject(0)));
    assertFalse(p.evaluate(new TestObject(1)));
  }

  @Test
  public void testLessThan() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.i()).lessThan(2);
    }};
    assertTrue(p.evaluate(new TestObject(1)));
    assertFalse(p.evaluate(new TestObject(2)));
    assertFalse(p.evaluate(new TestObject(3)));
  }

  @Test
  public void testLessThanOrEqual() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.i()).equalOrLessThan(2);
    }};
    assertTrue(p.evaluate(new TestObject(1)));
    assertTrue(p.evaluate(new TestObject(2)));
    assertFalse(p.evaluate(new TestObject(3)));
  }

  @Test
  public void testGreaterThan() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.i()).greaterThan(2);
    }};
    assertTrue(p.evaluate(new TestObject(3)));
    assertFalse(p.evaluate(new TestObject(2)));
    assertFalse(p.evaluate(new TestObject(1)));
  }

  @Test
  public void testGreaterThanOrEqual() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.i()).equalOrGreaterThan(2);
    }};
    assertTrue(p.evaluate(new TestObject(3)));
    assertTrue(p.evaluate(new TestObject(2)));
    assertFalse(p.evaluate(new TestObject(1)));
  }

  @Test
  public void testPattern() throws Exception {
    Predicate<TestObject> p = new PredicateDSL<TestObject>(){{
      expect(obj.attr()).matches("\\d+");
    }};
    assertTrue(p.evaluate(new TestObject("1424652")));
    assertFalse(p.evaluate(new TestObject("424652a1")));
  }

  private static class Person {
    @NotNull
    private String name;
    private Person parent;

    public Person() {}

    public Person(String name, Person parent) {
      this.name = name;
      this.parent = parent;
    }

    public Person parent() {
      return parent;
    }

    public String name() {
      return name;
    }
  }

  @Test
  public void testChainMethods() throws Exception {
    Predicate<Person> p = new PredicateDSL<Person>(){{
      expect(obj.parent().name()).equal("David");
    }};
    Person charles = new Person("Charles", null);
    Person david = new Person("David", null);

    Person cesar = new Person("Cesar", charles);
    Person john = new Person("John", david);

    assertTrue(p.evaluate(john));
    assertFalse(p.evaluate(cesar));
  }

  @Test
  public void testValidRestriction() throws Exception {
    Predicate<Person> p = new PredicateDSL<Person>(){{
      expect(obj.parent()).valid();
    }};

    Person charles = new Person("Charles", null);
    Person david = new Person("David", null);

    Person cesar = new Person("Cesar", charles);
    Person john = new Person("John", david);

    assertTrue(p.evaluate(john));
    assertTrue(p.evaluate(cesar));

    assertFalse(p.evaluate(new Person(null, new Person(null, null))));
  }

  @Test
  public void testInvalidRestriction() throws Exception {
    Predicate<Person> p = new PredicateDSL<Person>(){{
      expect(obj.parent()).invalid();
    }};

    Person charles = new Person("Charles", null);
    Person david = new Person("David", null);

    Person cesar = new Person("Cesar", charles);
    Person john = new Person("John", david);

    assertFalse(p.evaluate(john));
    assertFalse(p.evaluate(cesar));

    assertTrue(p.evaluate(new Person(null, new Person(null, null))));
  }

  @Test
  public void testMultipleStatements() throws Exception {
    Predicate<Element> predicate = new PredicateDSL<Element>() {{
      expect(obj.name()).matches("element.*");
      expect(obj.type()).equal(String.class);
    }};
    assertTrue(predicate.evaluate(mock(element().named("elementA").ofType(String.class))));
    assertFalse(predicate.evaluate(mock(element().named("elementB").ofType(Integer.class))));
  }

  @Test
  public void testBooleanStatement() throws Exception {
    Predicate<Element> predicate = new PredicateDSL<Element>() {{
      expect(obj.isWritable());
    }};
    assertTrue(predicate.evaluate(mock(element().writable())));
    assertFalse(predicate.evaluate(mock(element().nonWritable())));
  }

}
