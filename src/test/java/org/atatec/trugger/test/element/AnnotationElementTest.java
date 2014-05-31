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
import org.atatec.trugger.test.TestScenario;
import org.atatec.trugger.test.Should;
import org.atatec.trugger.util.mock.AnnotationMock;
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
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest {

  private Annotation annotation() {
    //@TestAnnotation(bool = false, name = "name", number = 1)
    return new AnnotationMock<TestAnnotation>() {{
      map(false).to(annotation.bool());
      map("name").to(annotation.name());
      map(1).to(annotation.number());
    }}.createMock();
  }

  @Test
  public void finderShouldReturnReadableElement() {
    TestScenario.given(element("name").in(TestAnnotation.class))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.be(readable())));
  }

  @Test
  public void finderShouldReturnNonWritableElement() {
    TestScenario.given(element("name").in(TestAnnotation.class))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.be(writable())));
  }

  @Test
  public void finderShouldReturnEmptyCollectionForAnnotationsWithoutElements() {
    TestScenario.given(elements().in(Documented.class))
        .thenIt(Should.BE_EMPTY);
  }

  @Test
  public void finderShouldReturnNonEmptyCollectionForAnnotationsWithElements() {
    TestScenario.given(elements().in(TestAnnotation.class))
        .thenIt(Should.NOT_BE_EMPTY)
        .each(Element.class, Should.notBe(writable()))
        .each(Element.class, Should.be(readable()));
  }

  @Test
  public void annotationElementTest() {
    TestScenario.given(element("bool").in(annotation()))
        .the(Element::type, Should.be(boolean.class))
        .the(Element::value, Should.BE_FALSE)

        .when((el) -> el.set(true))
        .thenIt(Should.raise(HandlingException.class));
  }

  private void assertAnnotationElement(Element element) {
    assertNotNull(element);
    assertTrue(Annotation.class.isAssignableFrom(element.declaringClass()));
    assertTrue(element.isReadable());
    assertFalse(element.isWritable());
  }

  @Test
  public void testNullSpecificElement() {
    //TestAnnotation annotation = AnnotationTestClass.class.getAnnotation(TestAnnotation.class);
    //Element el = element("non_existent").in(annotation);
    //assertNull(el);
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
        (el) -> el.in(new HandlingTest()).value());

    method = reflect().method("method").in(new HandlingTestNotAccess());
    assertThrow(HandlingException.class, element,
        (el) -> el.in(new HandlingTestNotAccess()).value());
  }

}
