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
package io.backpackcloud.trugger.util;

import org.junit.Test;

import java.lang.reflect.AnnotatedElement;

import static org.junit.Assert.*;

/**
 * @author Marcelo Guimaraes
 */
public class NullObjectsTest {

  @Test
  public void nullAnnotatedElementTest() {
    AnnotatedElement el = Null.NULL_ANNOTATED_ELEMENT;

    assertEquals(0, el.getAnnotations().length);
    assertEquals(0, el.getDeclaredAnnotations().length);
    assertFalse(el.isAnnotationPresent(null)); // annotation type doesn't matter
    assertNull(null, el.getAnnotation(null)); // annotation type doesn't matter
  }

}
