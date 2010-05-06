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
package net.sf.trugger.test.reflection;

import static net.sf.trugger.reflection.Reflection.wrapperFor;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ReflectionTests {

  @Test
  public void testWrapperResolver() {
    assertEquals(Boolean.class, wrapperFor(boolean.class));
    assertEquals(Byte.class, wrapperFor(byte.class));
    assertEquals(Short.class, wrapperFor(short.class));
    assertEquals(Character.class, wrapperFor(char.class));
    assertEquals(Integer.class, wrapperFor(int.class));
    assertEquals(Long.class, wrapperFor(long.class));
    assertEquals(Float.class, wrapperFor(float.class));
    assertEquals(Double.class, wrapperFor(double.class));
  }

}
