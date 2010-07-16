/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.format;

import static net.sf.trugger.util.mock.Mock.annotation;
import static net.sf.trugger.util.mock.Mock.mock;

import java.lang.annotation.Annotation;

import junit.framework.Assert;
import net.sf.trugger.element.Element;
import net.sf.trugger.factory.AnnotationFactoryContextImpl;
import net.sf.trugger.format.FormatException;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.format.FormatterFactory;
import net.sf.trugger.format.Formatters;
import net.sf.trugger.format.ParseException;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.test.TruggerTest;
import net.sf.trugger.util.mock.AnnotationMockBuilder;
import net.sf.trugger.validation.ValidatorContext;

import org.junit.Before;

/**
 * A base class for testing the formatters.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class FormatterTest<T extends Annotation> {

  protected final FormatterFactory factory = Formatters.factory();
  protected Formatter formatter;
  protected T annotation;
  protected AnnotationMockBuilder<T> builder;
  private final Class<T> annotationType;

  public FormatterTest() {
    annotationType = Reflection.reflect().genericType("T").in(this);
  }

  @Before
  public void initialize() {
    builder = new AnnotationMockBuilder<T>(annotationType);
    annotation = builder.annotation();
    formatter = null;
  }

  protected final void createFormatter(ValidatorContext context) {
    formatter = factory.create(context);
  }

  protected final void createFormatter(Annotation annotation, Object target) {
    formatter = factory.create(new AnnotationFactoryContextImpl(annotation, target));
  }

  protected final void createFormatter(Element element, Object target) {
    formatter = factory.create(new AnnotationFactoryContextImpl(builder.mock(), element, target));
  }

  protected final void createFormatter(Element element) {
    formatter = factory.create(new AnnotationFactoryContextImpl(builder.mock(), element));
  }

  protected final void createFormatter(Object target) {
    formatter = factory.create(new AnnotationFactoryContextImpl(builder.mock(), null, target));
  }

  protected final void createFormatter(Class<? extends Annotation> annotationType) {
    formatter = factory.create(new AnnotationFactoryContextImpl(mock(annotation(annotationType))));
  }

  protected final void createFormatter() {
    formatter = factory.create(new AnnotationFactoryContextImpl(builder.mock()));
  }

  protected final void assertTypeDisallowed(Object value) {
    if(formatter == null) {
      createFormatter();
    }
    boolean error = false;
    try {
      formatter.format(value);
    } catch (IllegalArgumentException e) {
      error = true;
    }
    Assert.assertTrue(error);
  }

  protected final void assertFormat(String expected, Object value) {
    if(formatter == null) {
      createFormatter();
    }
    Assert.assertEquals(expected, formatter.format(value));
  }

  protected final void assertFormatError(final Object value) {
    if(formatter == null) {
      createFormatter();
    }
    TruggerTest.assertThrow(new Runnable(){
      public void run() {
        formatter.format(value);
      }
    }, FormatException.class);
  }

  protected final void assertParse(Object expected, String value) {
    if(formatter == null) {
      createFormatter();
    }
    Assert.assertEquals(expected, formatter.parse(value));
  }

  protected final void assertParseError(final String value) {
    if(formatter == null) {
      createFormatter();
    }
    TruggerTest.assertThrow(new Runnable(){
      public void run() {
        formatter.parse(value);
      }
    }, ParseException.class);
  }

}
