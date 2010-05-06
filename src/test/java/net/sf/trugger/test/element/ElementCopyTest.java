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
package net.sf.trugger.test.element;

import static net.sf.trugger.element.Elements.copyTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.trugger.Transformer;
import net.sf.trugger.element.ElementCopy;
import net.sf.trugger.predicate.Predicate;

import org.junit.Before;
import org.junit.Test;

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

    copyTo(o2).elementsMatching(new Predicate<ElementCopy>() {

      public boolean evaluate(ElementCopy element) {
        return element.sourceElement().equals(element.destinationElement());
      }
    }).notNull().from(testObject);

    assertEquals(testObject.getAge(), o2.getAge());
    assertEquals(23, o2.getAge());
    assertEquals(testObject.getHeight(), o2.getHeight(), 0.001);
    assertEquals(1.9, o2.getHeight(), 0.01);
    assertEquals(testObject.getWeight(), o2.getWeight(), 0.01);
    assertEquals(80.2, o2.getWeight(), 0.01);
    assertEquals("Nick", o2.getNickName());
    assertNull(testObject.getNickName());
  }

  private static class ToStringTransformer implements Transformer<String, ElementCopy> {
    public String transform(ElementCopy object) {
      return String.valueOf(object.value());
    }
  }

  @Test
  public void testCopyToDifferent() {
    OtherTestObject o2 = new OtherTestObject();

    o2.setNickName("Nick");
    o2.setWeight(30.4);

    copyTo(o2).from(testObject);

    assertEquals(testObject.getWeight(), o2.getWeight(), 0.001);
    assertEquals(80.2, o2.getWeight(), 0.01);
    assertNull(o2.getNickName());
    assertNull(testObject.getNickName());

    Map<String, Object> map = new HashMap<String, Object>();

    copyTo(map).from(testObject);
    assertEquals(23, map.get("age"));
    assertEquals(null, map.get("nickName"));
    assertEquals("Marcelo", map.get("name"));
    assertEquals("Guimaraes", map.get("lastName"));
    assertEquals(1.9, (Double) map.get("height"), 0.001);
    assertEquals(80.2, (Double) map.get("weight"), 0.001);

    Properties props = new Properties();

    copyTo(props).transformingWith(new ToStringTransformer()).from(testObject);
    assertEquals("23", props.getProperty("age"));
    assertEquals("null", props.getProperty("nickName"));
    assertEquals("Marcelo", props.getProperty("name"));
    assertEquals("Guimaraes", props.getProperty("lastName"));
    assertEquals("1.9", props.getProperty("height"));
    assertEquals("80.2", props.getProperty("weight"));
  }
}
