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
package net.sf.trugger.test.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.lang.reflect.AnnotatedElement;

import net.sf.trugger.CreateException;
import net.sf.trugger.factory.AnnotationBasedFactory;
import net.sf.trugger.factory.Factories;
import net.sf.trugger.factory.Factory;

import org.junit.Before;
import org.junit.Test;

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
  }

  @Test(expected = CreateException.class)
  public void testNotAnnotated() {
    factory.create(NotAnnotated.class);
  }

  @Test
  public void testSharedObjectFactory() {
    Object o1 = factory.create(Direct.class);
    Object o2 = factory.create(Direct.class);

    assertNotSame(o1, o2);

    Factory sharedObjectFactory = Factories.sharedObjectFactory(factory);
    o1 = sharedObjectFactory.create(Direct.class);
    o2 = sharedObjectFactory.create(Direct.class);

    assertSame(o1, o2);
  }

  @ComponentClass(Object.class)
  public class Direct {

  }

  @ObjectComponent
  public class Nested {

  }

  public class NotAnnotated {

  }
}
