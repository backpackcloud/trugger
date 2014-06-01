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
import org.atatec.trugger.test.Should;
import org.atatec.trugger.test.TestScenario;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.atatec.trugger.element.Elements.copy;

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
        .when(weightIsSetTo(30.4))
        .and(nickNameIsSetTo("Nick"))
        .and(elementsAreCopiedFrom(testObject))
        .the(age(), Should.be(testObject.getAge()).andThen(Should.be(23)))
        .the(height(), Should.be(testObject.getHeight()).andThen(Should.be(1.9)))
        .the(weight(), Should.be(testObject.getWeight()).andThen(Should.be(80.2)))
        .the(nickName(), Should.notBe(testObject.getNickName()).andThen(Should.be("Nick")));
  }

  @Test
  public void testFilterCopy() {
    TestScenario.given(new TestObject("Marcelo", "Guimaraes"))
        .when(weightIsSetTo(30.4))
        .and(nickNameIsSetTo("Nick"))
        .and(ageIsSetTo(25))
        .and(elementsButAgeAreCopiedFrom(testObject))
        .the(age(), Should.notBe(testObject.getAge()).andThen(Should.be(25)))
        .the(height(), Should.be(testObject.getHeight()).andThen(Should.be(1.9)))
        .the(weight(), Should.be(testObject.getWeight()).andThen(Should.be(80.2)))
        .the(nickName(), Should.notBe(testObject.getNickName()).andThen(Should.be("Nick")));
  }

  private static class ToStringTransformer
      implements Function<ElementCopy, String> {

    public String apply(ElementCopy object) {
      return String.valueOf(object.value());
    }
  }

  @Test
  public void testCopyToDifferentTypes() {
    TestScenario.given(new OtherTestObject())
        .when(obj -> obj.setNickName("Nick"))
        .and(obj -> obj.setWeight(30.4))
        .and(obj -> copy().from(testObject).to(obj))

        .the(obj -> obj.getWeight(), Should.be(testObject.getWeight()))
        .the(obj -> obj.getNickName(), Should.be(testObject.getNickName()));

    TestScenario.given(new Properties())
        .when(props -> copy()
            .from(testObject)
            .notNull()
            .applying(new ToStringTransformer())
            .to(props))
        .the(property("age"), Should.be("23"))
        .the(property("nickName"), Should.BE_NULL)
        .the(property("name"), Should.be("Marcelo"))
        .the(property("lastName"), Should.be("Guimaraes"))
        .the(property("height"), Should.be("1.9"))
        .the(property("weight"), Should.be("80.2"));
  }

}
