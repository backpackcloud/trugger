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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import static org.atatec.trugger.element.Elements.copy;
import static org.atatec.trugger.element.Elements.elements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementCopyTest {

  private TestObject testObject;

  @Before
  public void init() {
    testObject = new TestObject("Marcelo", "Guimaraes");
    testObject.age = 23;
    testObject.setHeight(1.9);
    testObject.setWeight(80.2);
    testObject.setNickName(null);
  }

  @Test
  public void testCopyToSame() {
    TestObject o2 = new TestObject("Marcelo", "Guimaraes");

    o2.setNickName("Nick");
    o2.setWeight(30.4);

    copy().from(testObject).notNull().to(o2);

    assertEquals(testObject.getAge(), o2.getAge());
    assertEquals(23, o2.getAge());
    assertEquals(testObject.getHeight(), o2.getHeight(), 0.001);
    assertEquals(1.9, o2.getHeight(), 0.01);
    assertEquals(testObject.getWeight(), o2.getWeight(), 0.01);
    assertEquals(80.2, o2.getWeight(), 0.01);
    assertEquals("Nick", o2.getNickName());
    assertNull(testObject.getNickName());


    testObject.age = 50;
    testObject.setWeight(50);
    copy().from(testObject)
        .notNull()
        .filter(copy -> copy.dest().name().equals("age"))
        .to(o2);

    assertEquals(testObject.getAge(), o2.getAge());
    assertEquals(testObject.getHeight(), o2.getHeight(), 0.001);
    assertEquals(80.2, o2.getWeight(), 0.01);
  }

  private static class ToStringTransformer
      implements Function<ElementCopy, String> {

    public String apply(ElementCopy object) {
      return String.valueOf(object.value());
    }
  }

  @Test
  public void testCopyToDifferent() {
    OtherTestObject o2 = new OtherTestObject();

    o2.setNickName("Nick");
    o2.setWeight(30.4);

    copy().from(testObject).to(o2);

    assertEquals(testObject.getWeight(), o2.getWeight(), 0.001);
    assertEquals(80.2, o2.getWeight(), 0.01);
    assertNull(o2.getNickName());
    assertNull(testObject.getNickName());

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("weight", 10.0);

    copy().from(testObject)
        .filter(copy -> !copy.src().name().equals("weight"))
        .to(map);

    assertEquals(23, map.get("age"));
    assertEquals(null, map.get("nickName"));
    assertEquals("Marcelo", map.get("name"));
    assertEquals("Guimaraes", map.get("lastName"));
    assertEquals(1.9, (Double) map.get("height"), 0.001);
    assertEquals(10.0, (Double) map.get("weight"), 0.001);

    Properties props = new Properties();
    Function<ElementCopy, String> toString = new ToStringTransformer();

    copy().from(testObject).notNull().applying(toString).to(props);
    assertEquals("23", props.getProperty("age"));
    assertEquals(null, props.getProperty("nickName"));
    assertEquals("Marcelo", props.getProperty("name"));
    assertEquals("Guimaraes", props.getProperty("lastName"));
    assertEquals("1.9", props.getProperty("height"));
    assertEquals("80.2", props.getProperty("weight"));
  }

  static class Object1 {

    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

  static class Object2 {

    private int name;

    public void setName(int name) {
      this.name = name;
    }

    public int getName() {
      return name;
    }
  }

  @Test
  public void testCopyWithSameNameButDifferentTypes() throws Exception {
    Object1 object1 = new Object1();
    Object2 object2 = new Object2();

    object1.setName("name");

    copy(elements()).from(object1).to(object2);

    assertEquals(0, object2.getName());
  }

}
