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
package org.atatec.trugger.test;

import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.mock.Mock;
import org.atatec.trugger.util.mock.MockBuilder;
import org.easymock.IAnswer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static org.atatec.trugger.util.mock.Mock.annotation;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * A class for helping creation of simple {@link Element} mocks.
 *
 * @author Marcelo Varella Barca Guimarães
 * @see org.atatec.trugger.util.mock.Mock#mock(org.atatec.trugger.util.mock.MockBuilder)
 * @since 2.0
 */
public class ElementMock implements MockBuilder<Element> {

  private final Element element;
  private final List<Annotation> annotations;

  private class AnnotationsAnswer implements IAnswer<Annotation[]> {

    public Annotation[] answer() {
      return annotations.toArray(new Annotation[annotations.size()]);
    }
  }

  public ElementMock() {
    element = createNiceMock(Element.class);
    annotations = new ArrayList<Annotation>();
    IAnswer<Annotation[]> answer = new AnnotationsAnswer();
    expect(element.getAnnotations()).andAnswer(answer).anyTimes();
    expect(element.getDeclaredAnnotations()).andAnswer(answer).anyTimes();
  }

  /**
   * Sets the name for this element.
   *
   * @param name
   *          the desired name.
   * @return a reference to this object.
   */
  public ElementMock named(String name) {
    expect(element.name()).andReturn(name).anyTimes();
    return this;
  }

  /**
   * Sets an annotation to this element. The real annotation will be a mock
   * created by {@link org.atatec.trugger.util.mock.AnnotationMock}.
   *
   * @param type
   *          the annotation type.
   * @return a reference to this object.
   */
  public ElementMock annotatedWith(Class<? extends Annotation> type) {
    Annotation mock = Mock.mock(annotation(type));
    expect(element.isAnnotationPresent(type)).andReturn(true).anyTimes();
    expect((Annotation) element.getAnnotation(type)).andReturn(mock).anyTimes();
    annotations.add(mock);
    return this;
  }

  /**
   * Sets an annotation to this element.
   *
   * @param annotation
   *          the annotation.
   * @return a reference to this object.
   * @since 2.1
   */
  public ElementMock annotatedWith(Annotation annotation) {
    expect(element.isAnnotationPresent(annotation.annotationType())).andReturn(true).anyTimes();
    expect((Annotation) element.getAnnotation(annotation.annotationType())).andReturn(annotation).anyTimes();
    annotations.add(annotation);
    return this;
  }

  /**
   * Sets non readable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonReadable() {
    expect(element.isReadable()).andReturn(false).anyTimes();
    return this;
  }

  /**
   * Sets non specific for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonSpecific() {
    expect(element.isSpecific()).andReturn(false).anyTimes();
    return this;
  }

  /**
   * Sets non writable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock nonWritable() {
    expect(element.isWritable()).andReturn(false).anyTimes();
    return this;
  }

  /**
   * Sets the element type.
   *
   * @param type
   *          the element type.
   * @return a reference to this object.
   */
  public ElementMock ofType(Class type) {
    expect(element.type()).andReturn(type).anyTimes();
    return this;
  }

  /**
   * Sets readable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock readable() {
    expect(element.isReadable()).andReturn(true).anyTimes();
    return this;
  }

  /**
   * Sets specific for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock specific() {
    expect(element.isSpecific()).andReturn(true).anyTimes();
    return this;
  }

  /**
   * Sets writable for the element.
   *
   * @return a reference to this object.
   */
  public ElementMock writable() {
    expect(element.isWritable()).andReturn(true).anyTimes();
    return this;
  }

  /**
   * Sets the specific value for the element and {@link #specific()}.
   *
   * @param value
   *          the specific value.
   * @return a reference to this object.
   */
  public ElementMock withSpecificValue(Object value) {
    expect(element.value()).andReturn(value).anyTimes();
    return specific();
  }

  /**
   * Sets the handler for the given target.
   *
   * @param target
   *          the target related to the handler.
   * @param handler
   *          the handler for the given target.
   * @return a reference to this object.
   */
  public ElementMock withHandler(Object target, ValueHandler handler) {
    expect(element.in(target)).andReturn(handler).anyTimes();
    return this;
  }

  @Override
  public Element createMock() {
    replay(element);
    return element;
  }

}
