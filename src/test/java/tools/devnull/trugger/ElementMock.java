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
package tools.devnull.trugger;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import tools.devnull.trugger.element.Element;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.trugger.AnnotationMock.mockAnnotation;

/**
 * A class for helping creation of simple {@link Element} mocks.
 *
 * @author Marcelo "Ataxexe" Guimarães
 * @since 2.0
 */
public class ElementMock {

  private final Element element;
  private final List<Annotation> annotations;

  private class AnnotationsAnswer implements Answer<Annotation[]> {

    @Override
    public Annotation[] answer(InvocationOnMock invocation) throws Throwable {
      return annotations.toArray(new Annotation[annotations.size()]);
    }
  }

  public ElementMock() {
    element = mock(Element.class);
    annotations = new ArrayList<>();
    Answer<Annotation[]> returnAnnotations = new AnnotationsAnswer();
    when(element.getAnnotations()).then(returnAnnotations);
    when(element.getDeclaredAnnotations()).then(returnAnnotations);
  }

  /**
   * Sets the name for this element.
   *
   * @param name the desired name.
   * @return a reference to this object.
   */
  public ElementMock named(String name) {
    when(element.name()).thenReturn(name);
    return this;
  }

  /**
   * Sets an annotation to this element. The real annotation will be a mock.
   *
   * @param type the annotation type.
   * @return a reference to this object.
   */
  public ElementMock annotatedWith(Class<? extends Annotation> type) {
    Annotation mock = mockAnnotation(type);
    when(element.isAnnotationPresent(type)).thenReturn(true);
    when((Annotation) element.getAnnotation(type)).thenReturn(mock);
    annotations.add(mock);
    return this;
  }

  /**
   * Sets an annotation to this element.
   *
   * @param annotation the annotation.
   * @return a reference to this object.
   * @since 2.1
   */
  public ElementMock annotatedWith(Annotation annotation) {
    when(element.isAnnotationPresent(annotation.annotationType()))
        .thenReturn(true);
    when((Annotation) element.getAnnotation(annotation.annotationType()))
        .thenReturn(annotation);
    annotations.add(annotation);
    return this;
  }

  /**
   * Sets non readable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonReadable() {
    when(element.isReadable()).thenReturn(false);
    return this;
  }

  /**
   * Sets non specific for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonSpecific() {
    when(element.isSpecific()).thenReturn(false);
    return this;
  }

  /**
   * Sets non writable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonWritable() {
    when(element.isWritable()).thenReturn(false);
    return this;
  }

  /**
   * Sets the element type.
   *
   * @param type the element type.
   * @return a reference to this object.
   */
  public ElementMock ofType(Class type) {
    when(element.type()).thenReturn(type);
    return this;
  }

  /**
   * Sets readable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock readable() {
    when(element.isReadable()).thenReturn(true);
    return this;
  }

  /**
   * Sets specific for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock specific() {
    when(element.isSpecific()).thenReturn(true);
    return this;
  }

  /**
   * Sets writable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock writable() {
    when(element.isWritable()).thenReturn(true);
    return this;
  }

  /**
   * Sets the specific value for the element and {@link #specific()}.
   *
   * @param value the specific value.
   * @return a reference to this object.
   */
  public ElementMock withSpecificValue(Object value) {
    when(element.getValue()).thenReturn(value);
    return specific();
  }

  /**
   * Sets the handler for the given target.
   *
   * @param target  the target related to the handler.
   * @param handler the handler for the given target.
   * @return a reference to this object.
   */
  public ElementMock withHandler(Object target, ValueHandler handler) {
    when(element.on(target)).thenReturn(handler);
    return this;
  }

  public Element createMock() {
    return element;
  }

}
