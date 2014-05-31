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
import java.util.function.Consumer;
import java.util.function.Function;

import static org.atatec.trugger.element.ElementPredicates.readable;
import static org.atatec.trugger.element.ElementPredicates.writable;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.junit.Assert.assertNotNull;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest {

  private Annotation annotation() {
    // @TestAnnotation(bool = false, name = "name", number = 1)
    return new AnnotationMock<TestAnnotation>() {{
      map(false).to(annotation.bool());
      map("name").to(annotation.name());
      map(1).to(annotation.number());
    }}.createMock();
  }

  private Consumer<Element> shouldHaveAValue() {
    return (element) -> assertNotNull(element.value());
  }

  private Consumer<Element> attempToChangeValue() {
    return (element) -> element.set("a value");
  }

  private Function<Element, Object> elementValue() {
    return (element) -> element.value();
  }

  private Consumer<Element> attempToGetValue() {
    return (element) -> element.value();
  }

  @Test
  public void finderShouldReturnReadableElement() {
    TestScenario.given(element("name").in(annotation()))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.be(readable())))
        .the(elementValue(), Should.be("name"));

    TestScenario.given(element("name").in(TestAnnotation.class))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.be(readable())))
        .then(attempToGetValue(), Should.raise(NonSpecificElementException.class));
  }

  @Test
  public void finderShouldReturnNonWritableElement() {
    TestScenario.given(element("name").in(annotation()))
        .thenIt(Should.NOT_BE_NULL.andThen(Should.notBe(writable())))
        .the(elementValue(), Should.be("name"))
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
        .each(Element.class, Should.notBe(writable()))
        .each(Element.class, Should.be(readable()))
        .each(Element.class, shouldHaveAValue());
  }

  @Test
  public void testAnnotationElementAttributes() {
    TestScenario.given(element("bool").in(annotation()))
        .the(Element::type, Should.be(boolean.class))
        .the(elementValue(), Should.BE_FALSE)
        .the(Element::declaringClass, Should.be(TestAnnotation.class))
        .the(Element::name, Should.be("bool"))
        .the(Element::isSpecific, Should.BE_TRUE);
  }

}
