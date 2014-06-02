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
import org.atatec.trugger.element.NonSpecificElementException;
import org.atatec.trugger.test.Should;
import org.atatec.trugger.test.TestScenario;
import org.atatec.trugger.util.mock.AnnotationMock;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest extends BaseElementTest {

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
        .the(Element::name, Should.be("name"))
        .the(Element::value, Should.be("some name"))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.be(readable())));

    TestScenario.given(element("name").in(TestAnnotation.class))
        .it(Should.NOT_BE_NULL.andThen(Should.be(readable())))
        .then(attempToGetValue(), Should.raise(NonSpecificElementException.class));
  }

  @Test
  public void finderShouldReturnNonWritableElement() {
    TestScenario.given(element("name").in(annotation()))
        .the(Element::value, Should.be("some name"))
        .the(Element::name, Should.be("name"))
        .it(Should.NOT_BE_NULL.andThen(Should.notBe(writable())))
        .then(attempToChangeValue(), Should.raise(HandlingException.class));
  }

  @Test
  public void finderShouldReturnNullIfElementDoesNotExists() {
    TestScenario.given(element("non_existent").in(TestAnnotation.class))
        .thenIt(Should.BE_NULL);
  }

  @Test
  public void finderShouldReturnEmptyCollectionForAnnotationsWithoutElements() {
    TestScenario.given(elements().in(Documented.class))
        .thenIt(Should.BE_EMPTY);
  }

  @Test
  public void finderShouldReturnNonEmptyCollectionForAnnotationsWithElements() {
    TestScenario.given(elements().in(annotation()))
        .thenIt(Should.NOT_BE_EMPTY)
        .each(Should.notBe(writable()))
        .each(Should.be(readable()))
        .each(shouldHaveAValue());
  }

  @Test
  public void testAnnotationElementAttributes() {
    TestScenario.given(element("bool").in(annotation()))
        .the(Element::type, Should.be(boolean.class))
        .the(Element::value, Should.BE_FALSE)
        .the(Element::declaringClass, Should.be(TestAnnotation.class))
        .the(Element::name, Should.be("bool"))
        .the(Element::isSpecific, Should.BE_TRUE);
  }

}
