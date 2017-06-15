/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.kodo.Spec;
import tools.devnull.trugger.HandlingException;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static tools.devnull.kodo.Expectation.*;
import static org.mockito.Mockito.when;
import static tools.devnull.trugger.AnnotationMock.mockAnnotation;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest implements ElementSpecs {

  private Annotation annotation() {
    // @TestAnnotation(bool = false, name = "some name", number = 1)
    TestAnnotation annotation = mockAnnotation(TestAnnotation.class);
    when(annotation.bool()).thenReturn(false);
    when(annotation.name()).thenReturn("some name");
    when(annotation.number()).thenReturn(1);
    return annotation;
  }

  @Test
  public void testElementSpecs() {
    Spec.given(element("name").in(annotation()))
        .expect(Element::name, to().be("name"))
        .expect(Element::value, to().be("some name"))
        .expect(stringRepresentation(), to().be("name : java.lang.String"))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().be(readable()))
        .expect(attempToChangeValue(), to().raise(HandlingException.class));

    Spec.given(element("name").in(TestAnnotation.class))
        .expect(it(), to().not().beNull())
        .expect(it(), to().not().be(writable()))
        .expect(it(), to().be(readable()))

        .expect(attempToGetValue(), to().raise(NonSpecificElementException.class));

    Spec.given(element("bool").in(annotation()))
        .expect(Element::type, to().be(boolean.class))
        .expect(Element::value, to().be(false))
        .expect(Element::declaringClass, to().be(TestAnnotation.class))
        .expect(Element::name, to().be("bool"))
        .expect(it(), to().be(specific()));
  }

  @Test
  public void testElements() {
    List<Element> elements = elements().in(annotation());
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
    assertNull(element("non_existent").in(annotation()));
    assertNull(element("non_existent").in(TestAnnotation.class));
    assertTrue(elements().in(Documented.class).isEmpty());
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

    Spec.given(element("name").in(annotation))
        .expect(attempToGetValue(), to().raise(HandlingException.class));
  }

}
