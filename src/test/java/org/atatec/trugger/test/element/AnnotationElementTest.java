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
import org.atatec.trugger.element.NonSpecificElementException;
import org.atatec.trugger.util.mock.AnnotationMock;
import org.junit.Test;
import org.kodo.TestScenario;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.kodo.Spec.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest implements ElementSpecs {

  private Annotation annotation() {
    // @TestAnnotation(bool = false, name = "name", number = 1)
    return new AnnotationMock<TestAnnotation>() {{
      map(false).to(annotation.bool());
      map("some name").to(annotation.name());
      map(1).to(annotation.number());
    }}.createMock();
  }

  @Test
  public void finderShouldReturnReadableElement() {
    TestScenario.given(element("name").in(annotation()))
        .the(name(), should(be("name")))
        .the(value(), should(be("some name")))
        .thenIt(should(be(NOT_NULL)))
        .and(should(be(readable())));

    TestScenario.given(element("name").in(TestAnnotation.class))
        .it(should(be(NOT_NULL)))
        .and(should(be(readable())))
        .then(attempToGetValue(), should(raise(NonSpecificElementException.class)));
  }

  @Test
  public void finderShouldReturnNonWritableElement() {
    TestScenario.given(element("name").in(annotation()))
        .the(value(), should(be("some name")))
        .the(name(), should(be("name")))
        .thenIt(should(be(NOT_NULL)))
        .and(should(notBe(writable())))
        .then(attempToChangeValue(), should(raise(HandlingException.class)));
  }

  @Test
  public void finderShouldReturnNullIfElementDoesNotExists() {
    TestScenario.given(element("non_existent").in(TestAnnotation.class))
        .thenIt(should(be(NULL)));
  }

  @Test
  public void finderShouldReturnEmptyCollectionForAnnotationsWithoutElements() {
    TestScenario.given(elements().in(Documented.class))
        .thenIt(should(be(EMPTY)));
  }

  @Test
  public void finderShouldReturnNonEmptyCollectionForAnnotationsWithElements() {
    TestScenario.given(elements().in(annotation()))
        .thenIt(should(notBe(EMPTY)))
        .each(should(notBe(writable())))
        .each(should(be(readable())))
        .each(should(have(aValue())));
  }

  @Test
  public void testAnnotationElementAttributes() {
    TestScenario.given(element("bool").in(annotation()))
        .the(type(), should(be(boolean.class)))
        .the(value(), should(be(FALSE)))
        .the(declaringClass(), should(be(TestAnnotation.class)))
        .the(name(), should(be("bool")))
        .it(should(be(specific())));
  }

  @Test
  public void testNoElementFound() {
    TestScenario.given(element("notExist").in(annotation()))
        .it(should(be(NULL)));
  }

}
