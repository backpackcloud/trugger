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
package io.backpackcloud.trugger.element;

import org.junit.Before;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static io.backpackcloud.kodo.Expectation.to;
import static io.backpackcloud.trugger.element.ElementPredicates.ofName;
import static io.backpackcloud.trugger.element.Elements.copy;
import static io.backpackcloud.trugger.element.Elements.elements;

/**
 * @author Marcelo Guimaraes
 */
public class ElementCopyTest {

  private TestObject testObject;

  private Function<Properties, String> property(String name) {
    return props -> props.getProperty(name);
  }

  @Before
  public void init() {
    testObject = new TestObject("Marcelo", "Guimaraes");
    testObject.age = 23;
    testObject.setHeight(1.9);
    testObject.setWeight(80.2);
    testObject.setNickName(null);
  }

  private Function<TestObject, ?> age() {
    return (obj) -> obj.getAge();
  }

  private Function<TestObject, ?> nickName() {
    return (obj) -> obj.getNickName();
  }

  private Function<TestObject, ?> name() {
    return (obj) -> obj.getName();
  }

  private Function<TestObject, ?> lastName() {
    return (obj) -> obj.getLastName();
  }

  private Function<TestObject, ?> weight() {
    return (obj) -> obj.getWeight();
  }

  private Function<TestObject, ?> height() {
    return (obj) -> obj.getHeight();
  }

  private Consumer<TestObject> ageIsSetTo(int value) {
    return (obj) -> obj.setAge(value);
  }

  private Consumer<TestObject> weightIsSetTo(double value) {
    return (obj) -> obj.setWeight(value);
  }

  private Consumer<TestObject> nickNameIsSetTo(String nick) {
    return (obj) -> obj.setNickName(nick);
  }

  private Consumer<TestObject> elementsAreCopiedFrom(TestObject o) {
    return (obj) -> copy().from(o).notNull().to(obj);
  }

  private Consumer<TestObject> elementsButAgeAreCopiedFrom(TestObject o) {
    return (obj) -> copy().from(o).notNull()
        .filter(copy -> !copy.dest().name().equals("age"))
        .to(obj);
  }

  @Test
  public void testCopyToSame() {
    Spec.given(new TestObject("Marcelo", "Guimaraes"))
        .when(weightIsSetTo(30.4)
            .andThen(nickNameIsSetTo("Nick"))
            .andThen(elementsAreCopiedFrom(testObject)))

        .expect(age(), to().be(testObject.getAge()))
        .expect(age(), to().be(23))

        .expect(height(), to().be(testObject.getHeight()))
        .expect(height(), to().be(1.9))

        .expect(weight(), to().be(testObject.getWeight()))
        .expect(weight(), to().be(80.2))

        .expect(nickName(), to().not().be(testObject.getNickName()))
        .expect(nickName(), to().be("Nick"));
  }

  @Test
  public void testFilterCopy() {
    Spec.given(new TestObject("Marcelo", "Guimaraes"))
        .when(weightIsSetTo(30.4)
            .andThen(nickNameIsSetTo("Nick"))
            .andThen(ageIsSetTo(25))
            .andThen(elementsButAgeAreCopiedFrom(testObject)))
        .expect(age(), to().not().be(testObject.getAge()))
        .expect(age(), to().be(25))

        .expect(height(), to().be(testObject.getHeight()))
        .expect(height(), to().be(1.9))

        .expect(weight(), to().be(testObject.getWeight()))
        .expect(weight(), to().be(80.2))

        .expect(nickName(), to().not().be(testObject.getNickName()))
        .expect(nickName(), to().be("Nick"));
  }

  private static class ToStringTransformer
      implements Function<ElementCopy, String> {

    public String apply(ElementCopy object) {
      return String.valueOf((Object) object.value());
    }
  }

  @Test
  public void testCopyToDifferentTypes() {
    Consumer<OtherTestObject> nickNameIsChanged = obj -> obj.setNickName("Nick");
    Consumer<OtherTestObject> weightIsChanged = obj -> obj.setWeight(30.4);
    Consumer<OtherTestObject> elementsAreCopiedFromTestObject =
        obj -> copy().from(testObject).to(obj);

    Function<OtherTestObject, Object> weight = OtherTestObject::getWeight;
    Function<OtherTestObject, Object> nickName = OtherTestObject::getNickName;

    Spec.given(new OtherTestObject())
        .when(nickNameIsChanged
            .andThen(weightIsChanged)
            .andThen(elementsAreCopiedFromTestObject))

        .expect(weight, to().be(testObject.getWeight()))
        .expect(nickName, to().be(testObject.getNickName()));

    Spec.given(new Properties())
        .when(props -> copy()
            .from(testObject)
            .notNull()
            .map(new ToStringTransformer())
            .to(props))
        .expect(property("age"), to().be("23"))
        .expect(property("nickName"), to().beNull())
        .expect(property("name"), to().be("Marcelo"))
        .expect(property("lastName"), to().be("Guimaraes"))
        .expect(property("height"), to().be("1.9"))
        .expect(property("weight"), to().be("80.2"));
  }

  @Test
  public void testCopyWithSelector() {
    Spec.given(new TestObject("John", "Smith"))
        .when(o -> copy(elements().filter(e -> false)).from(testObject).to(o))
        .expect(name(), to().be("John"))
        .expect(lastName(), to().be("Smith"))
        .expect(nickName(), to().beNull())
        .expect(age(), to().be(0))
        .expect(height(), to().be(0.0))
        .expect(weight(), to().be(0.0));
  }

  @Test
  public void testFilter() {
    copy(elements().filter(ofName("name")))
        .from(testObject)
        .filter(copy -> assertFilterForDifferentType(copy))
        .to(new Properties());

    copy(elements().filter(ofName("name")))
        .from(testObject)
        .filter(copy -> assertFilterForSameType(copy))
        .to(new TestObject(null, null));
  }

  private void assertFilter(ElementCopy copy) {
    assertEquals("name", copy.src().name());
    assertEquals("name", copy.dest().name());
    assertEquals("Marcelo", copy.src().getValue());
    assertEquals(String.class, copy.src().type());
    assertEquals(String.class, copy.dest().type());
  }

  private boolean assertFilterForDifferentType(ElementCopy copy) {
    assertFilter(copy);
    assertNotEquals(copy.src().declaringClass(), copy.dest().declaringClass());
    return true;
  }

  private boolean assertFilterForSameType(ElementCopy copy) {
    assertFilter(copy);
    assertEquals(copy.src().declaringClass(), copy.dest().declaringClass());
    return true;
  }

}
