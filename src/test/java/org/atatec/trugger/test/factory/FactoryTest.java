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
package org.atatec.trugger.test.factory;

import org.atatec.trugger.CreateException;
import org.atatec.trugger.factory.AnnotationBasedFactory;
import org.atatec.trugger.factory.Factory;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Factories.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class FactoryTest {

  private Factory<AnnotatedElement, Object> factory;

  @Before
  public void initialize() {
    factory = new AnnotationBasedFactory<ComponentClass, Object>(){};
  }

  /**
   * Tests if the factory can create the object using the annotation in the
   * target.
   */
  @Test
  public void testDirectAnnotation() {
    Object o = factory.create(Direct.class);
    assertEquals(Object.class, o.getClass());
  }

  /**
   * Tests if the factory can create the object using the annotation in one of
   * the annotations present in the target annotations.
   */
  @Test
  public void testNestedAnnotation() {
    Object o = factory.create(Nested.class);
    assertEquals(Object.class, o.getClass());

    o = factory.create(Deep.class);
    assertEquals(Object.class, o.getClass());
  }

  @Test(expected = CreateException.class)
  public void testNotAnnotated() {
    factory.create(NotAnnotated.class);
  }

  @ComponentClass(Object.class)
  public class Direct {

  }

  @ObjectComponent
  public class Nested {

  }

  @MyAnnotation
  public class Deep {

  }

  @ObjectComponent
  @Retention(RetentionPolicy.RUNTIME)
  public @interface MyAnnotation{}

  public class NotAnnotated {

  }
}
