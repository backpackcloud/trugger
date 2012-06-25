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
package org.atatec.trugger.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.AnnotatedElement;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.atatec.trugger.util.Null;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
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
  
  @Test(expected = NoSuchElementException.class)
  public void nullResourceBundleTest() {
    ResourceBundle bundle = Null.NULL_BUNDLE;
    
    assertFalse(bundle.getKeys().hasMoreElements());
    
    assertNotNull(bundle.getLocale());
    bundle.getKeys().nextElement();
  }
  
}
