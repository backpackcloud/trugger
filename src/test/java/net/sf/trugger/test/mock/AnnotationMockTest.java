/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.mock;

import static net.sf.trugger.util.mock.Mock.annotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;

import net.sf.trugger.util.mock.AnnotationMockBuilder;

import org.junit.Test;


/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationMockTest {

  private Resource resource;
	private Resource resource2;

	public AnnotationMockTest() {
		resource = new AnnotationMockBuilder<Resource>() {{
      map("name").to(annotation.name());
      map(false).to(annotation.shareable());
    }}.mock();

		AnnotationMockBuilder<Resource> builder = annotation(Resource.class);
		resource2 = builder.annotation();
		builder.map("name2").to(resource2.name());
		builder.map(true).to(resource2.shareable());
		builder.map(AuthenticationType.APPLICATION).to(resource2.authenticationType());
		builder.mock();
	}

  @Test
  public void testAnnotationType() {
    assertEquals(Resource.class, resource.annotationType());
    assertEquals(Resource.class, resource2.annotationType());
  }

  @Test
  public void testDefinedProperties() {
    assertEquals("name", resource.name());
    assertEquals("name2", resource2.name());
    assertFalse(resource.shareable());
    assertTrue(resource2.shareable());
  }

  @Test
  public void testDefaultProperties() {
    assertEquals(Object.class, resource.type());
    assertEquals(Object.class, resource2.type());
    assertEquals("", resource.mappedName());
    assertEquals("", resource2.mappedName());
    assertEquals("", resource.description());
    assertEquals("", resource2.description());
    assertEquals(AuthenticationType.CONTAINER, resource.authenticationType());
    assertEquals(AuthenticationType.APPLICATION, resource2.authenticationType());
  }

	@Test(expected = IllegalStateException.class)
	public void testDefineAnnotationType() {
		new AnnotationMockBuilder<Resource>() {{
      map(Annotation.class).to((Class) annotation.annotationType());
    }}.mock();
	}

}
