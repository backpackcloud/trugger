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

package io.backpackcloud.trugger.util.factory;

import io.backpackcloud.trugger.AnnotationMock;
import io.backpackcloud.trugger.ElementMock;
import org.junit.Before;
import org.junit.Test;
import io.backpackcloud.trugger.Flag;
import io.backpackcloud.trugger.element.Element;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComponentFactoryTest {

  private ComponentFactory<ConverterClass, Converter> factory;

  @Before
  public void initialize() {
    factory = new ComponentFactory<>(ConverterClass.class);
  }

  @Test
  public void testCreate() {
    ToString annotation = AnnotationMock.mockAnnotation(ToString.class);

    Converter converter = factory.create(annotation).get();
    assertEquals("10", converter.convert(10));
    assertEquals("null", converter.convert(null));
  }

  @Test
  public void testListCreate() {
    Element element = new ElementMock()
        .annotatedWith(AnnotationMock.mockAnnotation(Flag.class))
        .annotatedWith(AnnotationMock.mockAnnotation(ToNull.class))
        .annotatedWith(AnnotationMock.mockAnnotation(Resource.class))
        .annotatedWith(AnnotationMock.mockAnnotation(ToString.class))
        .createMock();

    List<Converter> converters = factory.createAll(element);
    assertEquals(2, converters.size());
  }

  @Test
  public void testElementCreate() {
    Element element = new ElementMock()
        .annotatedWith(AnnotationMock.mockAnnotation(Resource.class))
        .annotatedWith(AnnotationMock.mockAnnotation(ToString.class))
        .annotatedWith(AnnotationMock.mockAnnotation(ToNull.class))
        .annotatedWith(AnnotationMock.mockAnnotation(Flag.class))
        .createMock();

    Converter converter = factory.create(element).get();
    assertNotNull(converter);

    element = new ElementMock()
        .annotatedWith(AnnotationMock.mockAnnotation(Resource.class))
        .annotatedWith(AnnotationMock.mockAnnotation(Flag.class))
        .createMock();

    converter = factory.create(element).orElse(null);
    assertNull(converter);
  }

  @Test
  public void testTypeMatchingConstructor() {
    Dummy annotation = AnnotationMock.mockAnnotation(Dummy.class);
    when(annotation.value()).thenReturn("dummy");

    Element element = new ElementMock()
        .annotatedWith(annotation)
        .createMock();

    Converter converter = factory.create(element).get();
    assertEquals("dummy", converter.convert(new Object()));
  }

  @Test
  public void testConfiguration() {
    BiConsumer<Context, Annotation> biconsumer = mock(BiConsumer.class);
    Dummy annotation = AnnotationMock.mockAnnotation(Dummy.class);
    when(annotation.value()).thenReturn("dummy");

    Element element = new ElementMock()
        .annotatedWith(annotation)
        .createMock();

    factory.toConfigure(biconsumer).create(element);
    verify(biconsumer).accept(any(Context.class), eq(annotation));
  }

  @Test
  public void testInstantiation() {
    BiFunction<Constructor, Object[], Object> bifunction = mock(BiFunction.class);
    Dummy annotation = AnnotationMock.mockAnnotation(Dummy.class);
    Converter converter = new DummyConverter("");

    when(bifunction.apply(any(Constructor.class), any(Object[].class))).thenReturn(converter);
    when(annotation.value()).thenReturn("dummy");

    Element element = new ElementMock()
        .annotatedWith(annotation)
        .createMock();

    assertSame(converter, factory.toCreate(bifunction).create(element).get());
    verify(bifunction).apply(any(Constructor.class), any(Object[].class));
  }

}
