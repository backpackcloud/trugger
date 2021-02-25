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

import com.backpackcloud.trugger.AnnotationMock;
import com.backpackcloud.trugger.HandlingException;
import org.junit.Test;
import io.backpackcloud.kodo.Spec;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static io.backpackcloud.kodo.Expectation.*;
import static org.mockito.Mockito.when;
import static com.backpackcloud.trugger.element.ElementPredicates.*;
import static com.backpackcloud.trugger.element.Elements.element;
import static com.backpackcloud.trugger.element.Elements.elements;

/**
 * @author Marcelo Guimaraes
 */
public class AnnotationElementTest implements ElementExpectations {

  private Annotation annotation() {
    // @TestAnnotation(bool = false, name = "some name", number = 1)
    TestAnnotation annotation = AnnotationMock.mockAnnotation(TestAnnotation.class);
    when(annotation.bool()).thenReturn(false);
    when(annotation.name()).thenReturn("some name");
    when(annotation.number()).thenReturn(1);
    return annotation;
  }

  @Test
  public void testElementSpecs() {
    Spec.given(element("name").from(annotation()).orElse(null))
        .expect(Element::name, to().be("name"))
        .expect(Element::getValue, to().be("some name"))
        .expect(stringRepresentation(), to().be("name : java.lang.String"))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().be(readable()))
        .expect(attempToChangeValue(), to().raise(HandlingException.class));

    Spec.given(element("name").from(TestAnnotation.class).orElse(null))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().be(readable()))

        .expect(getValue(), to().raise(NonSpecificElementException.class));

    Spec.given(element("bool").from(annotation()).orElse(null))
        .expect(Element::type, to().be(boolean.class))
        .expect(Element::getValue, to().be(false))
        .expect(Element::declaringClass, to().be(TestAnnotation.class))
        .expect(Element::name, to().be("bool"))
        .expect(it(), to().be(specific()));
  }

  @Test
  public void testElements() {
    List<Element> elements = elements().from(annotation());
    assertFalse(elements.isEmpty());

    for (Element element : elements) {
      Spec.given(element)
          .expect(it(), to().not().be(writable()))
          .expect(it(), to().be(readable()))
          .expect(it(), to().have(aValue()));
    }
  }

  @Test
  public void testNoElementFound() {
    assertNull(element("non_existent").from(annotation()).orElse(null));
    assertNull(element("non_existent").from(TestAnnotation.class).orElse(null));
    assertTrue(elements().from(Documented.class).isEmpty());
  }

  @Test
  public void testInvokeErrors() {
    TestAnnotation annotation = new TestAnnotation() {

      @Override
      public boolean bool() {
        return false;
      }

      @Override
      public String name() {
        throw new RuntimeException();
      }

      @Override
      public int number() {
        throw new IllegalArgumentException();
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return TestAnnotation.class;
      }
    };

    Spec.given(element("name").from(annotation).get())
        .expect(getValue(), to().raise(HandlingException.class));
  }

}
