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
package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.kodo.TestScenario;
import tools.devnull.trugger.HandlingException;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;

import static tools.devnull.kodo.Spec.*;
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
    TestScenario.given(element("name").in(annotation()))
        .the(name(), should(be("name")))
        .the(value(), should(be("some name")))
        .the(stringRepresentation(), should(be("name : java.lang.String")))
        .thenIt(should(be(NOT_NULL)))
        .and(should(notBe(writable())))
        .and(should(be(readable())))
        .then(attempToChangeValue(), should(raise(HandlingException.class)));

    TestScenario.given(element("name").in(TestAnnotation.class))
        .it(should(be(NOT_NULL)))
        .and(should(notBe(writable())))
        .and(should(be(readable())))
        .then(attempToGetValue(), should(raise(NonSpecificElementException.class)));

    TestScenario.given(element("bool").in(annotation()))
        .the(type(), should(be(boolean.class)))
        .the(value(), should(be(FALSE)))
        .the(declaringClass(), should(be(TestAnnotation.class)))
        .the(name(), should(be("bool")))
        .it(should(be(specific())));
  }

  @Test
  public void testElements() {
    TestScenario.given(elements().in(annotation()))
        .thenIt(should(notBe(EMPTY)))
        .each(should(notBe(writable())))
        .each(should(be(readable())))
        .each(should(have(aValue())));
  }

  @Test
  public void testNoElementFound() {
    TestScenario.given(element("non_existent").in(annotation()))
        .it(should(be(NULL)));

    TestScenario.given(element("non_existent").in(TestAnnotation.class))
        .thenIt(should(be(NULL)));

    TestScenario.given(elements().in(Documented.class))
        .thenIt(should(be(EMPTY)));
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

    TestScenario.given(element("name").in(annotation))
        .then(attempToGetValue(), should(raise(HandlingException.class)));
  }

}
