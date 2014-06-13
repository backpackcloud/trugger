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

import org.atatec.trugger.element.ElementCopy;
import org.junit.Before;
import org.junit.Test;
import org.kodo.TestScenario;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.atatec.trugger.element.Elements.copy;
import static org.atatec.trugger.element.Elements.elements;
import static org.kodo.Spec.*;

/**
 * @author Marcelo Varella Barca Guimarães
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
    TestScenario.given(new TestObject("Marcelo", "Guimaraes"))
        .when(weightIsSetTo(30.4)
            .andThen(nickNameIsSetTo("Nick"))
            .andThen(elementsAreCopiedFrom(testObject)))
        .the(age(), should(be(testObject.getAge())).and(should(be(23))))
        .the(height(), should(be(testObject.getHeight())).and(should(be(1.9))))
        .the(weight(), should(be(testObject.getWeight())).and(should(be(80.2))))
        .the(nickName(), should(notBe(testObject.getNickName()))
            .and(should(be("Nick"))));
  }

  @Test
  public void testFilterCopy() {
    TestScenario.given(new TestObject("Marcelo", "Guimaraes"))
        .when(weightIsSetTo(30.4)
            .andThen(nickNameIsSetTo("Nick"))
            .andThen(ageIsSetTo(25))
            .andThen(elementsButAgeAreCopiedFrom(testObject)))
        .the(age(), should(notBe(testObject.getAge())).and(should(be(25))))
        .the(height(), should(be(testObject.getHeight())).and(should(be(1.9))))
        .the(weight(), should(be(testObject.getWeight())).and(should(be(80.2))))
        .the(nickName(), should(notBe(testObject.getNickName()))
            .and(should(be("Nick"))));
  }

  private static class ToStringTransformer
      implements Function<ElementCopy, String> {

    public String apply(ElementCopy object) {
      return String.valueOf(object.value());
    }
  }

  @Test
  public void testCopyToDifferentTypes() {
    Consumer<OtherTestObject> nickNameIsChanged = obj -> obj.setNickName("Nick");
    Consumer<OtherTestObject> weightIsChanged = obj -> obj.setWeight(30.4);
    Consumer<OtherTestObject> elementsAreCopiedFromTestObject =
        obj -> copy().from(testObject).to(obj);

    Function<OtherTestObject, Object> weight = obj -> obj.getWeight();
    Function<OtherTestObject, Object> nickName = obj -> obj.getNickName();

    TestScenario.given(new OtherTestObject())
        .when(nickNameIsChanged
            .andThen(weightIsChanged)
            .andThen(elementsAreCopiedFromTestObject))

        .the(weight, should(be(testObject.getWeight())))
        .the(nickName, should(be(testObject.getNickName())));

    TestScenario.given(new Properties())
        .when(props -> copy()
            .from(testObject)
            .notNull()
            .applying(new ToStringTransformer())
            .to(props))
        .the(property("age"), should(be("23")))
        .the(property("nickName"), should(be(NULL)))
        .the(property("name"), should(be("Marcelo")))
        .the(property("lastName"), should(be("Guimaraes")))
        .the(property("height"), should(be("1.9")))
        .the(property("weight"), should(be("80.2")));
  }

  @Test
  public void testCopyWithSelector() {
    TestScenario.given(new TestObject("John", "Smith"))
        .when(o -> copy(elements().filter(e -> false)).from(testObject).to(o))
        .the(name(), should(be("John")))
        .the(lastName(), should(be("Smith")))
        .the(nickName(), should(be(NULL)))
        .the(age(), should(be(0)))
        .the(height(), should(be(0.0)))
        .the(weight(), should(be(0.0)));
  }

}
