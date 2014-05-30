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

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.AnnotationElement;
import org.atatec.trugger.test.Scenario;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.Method;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.atatec.trugger.test.Specs.*;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest {

  @TestAnnotation(bool = false, name = "name", number = 1)
  private class AnnotationTestClass {
  }

  @Test
  public void finderShouldReturnReadableElement() {
    Scenario.given(element("name").in(TestAnnotation.class))
        .thenIt(shouldNotBeNull())
        .and(shouldBe(readable()));
  }

  @Test
  public void finderShouldReturnNonWritableElement() {
    Scenario.given(element("name").in(TestAnnotation.class))
        .thenIt(shouldNotBeNull())
        .and(shouldNotBe(writable()));
  }

  @Test
  public void finderShouldReturnEmptyCollectionForAnnotationsWithoutElements() {
    Scenario.given(elements().in(Documented.class))
        .thenIt(shouldBeEmpty());
  }

  @Test
  public void finderShouldReturnNonEmptyCollectionForAnnotationsWithElements() {
    Scenario.given(elements().in(TestAnnotation.class))
        .thenIt(shouldNotBeEmpty())
        .thenEach(shouldNotBe(writable()))
        .andEach(shouldBe(readable()));
  }

  @Test
  public void annotationElementTest() {
    TestAnnotation annotation = AnnotationTestClass.class.getAnnotation(TestAnnotation.class);
    final Element specific = element("bool").in(annotation);
    assertEquals(boolean.class, specific.type());
    assertEquals(false, specific.get());
    assertThrow(HandlingException.class, () -> specific.set(true));
  }

  private void assertAnnotationElement(Element element) {
    assertNotNull(element);
    assertTrue(Annotation.class.isAssignableFrom(element.declaringClass()));
    assertTrue(element.isReadable());
    assertFalse(element.isWritable());
  }

  @Test
  public void testNullSpecificElement() {
    TestAnnotation annotation = AnnotationTestClass.class.getAnnotation(TestAnnotation.class);
    Element el = element("non_existent").in(annotation);
    assertNull(el);
  }

  public static class HandlingTest {
    public void method() {
      throw new IllegalArgumentException();
    }
  }

  class HandlingTestNotAccess {
    public void method() {
      throw new IllegalArgumentException();
    }
  }

  @Test
  public void testHandlingException() {
    Method method = reflect().method("method").in(HandlingTest.class);
    AnnotationElement element = new AnnotationElement(method);
    assertThrow(HandlingException.class, element,
        (el) -> el.in(new HandlingTest()).get());

    method = reflect().method("method").in(new HandlingTestNotAccess());
    assertThrow(HandlingException.class, element,
        (el) -> el.in(new HandlingTestNotAccess()).get());
  }

}
