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
package net.sf.trugger.test.general;

import static net.sf.trugger.util.Utils.isTypeAccepted;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.trugger.annotation.AcceptArrays;
import net.sf.trugger.annotation.AcceptedArrayTypes;
import net.sf.trugger.annotation.AcceptedTypes;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AcceptedTypesTest {

  @AcceptedTypes(String.class)
  private class OnlyString {}

  @AcceptedArrayTypes(String.class)
  private class OnlyStringArray {}

  @AcceptedArrayTypes(Object.class)
  private class OnlyArray {}

  @AcceptedTypes(int.class)
  private class OnlyInt {}

  @AcceptedArrayTypes(int.class)
  private class OnlyIntArray {}

  @AcceptedTypes(CharSequence.class)
  private class OnlyCharSequence {}

  @AcceptedArrayTypes(CharSequence.class)
  private class OnlyCharSequenceArray {}

  @AcceptedTypes( { int.class, String.class })
  private class IntOrString {}

  @AcceptedArrayTypes( { int.class, String.class })
  private class IntOrStringArray {}

  @AcceptArrays
  private class AnyArrayType {}

  @Test
  public void testTrueAcceptances() {
    assertTrue(isTypeAccepted(String.class, OnlyString.class));
    assertTrue(isTypeAccepted(int.class, OnlyInt.class));
    assertTrue(isTypeAccepted(Integer.class, OnlyInt.class));
    assertTrue(isTypeAccepted(String.class, OnlyCharSequence.class));
    assertTrue(isTypeAccepted(Object.class, Object.class));
  }

  @Test
  public void testFalseAcceptances() {
    assertFalse(isTypeAccepted(Object.class, OnlyString.class));
    assertFalse(isTypeAccepted(int.class, OnlyString.class));
    assertFalse(isTypeAccepted(CharSequence.class, OnlyString.class));
    assertFalse(isTypeAccepted(Object.class, OnlyStringArray.class));
    assertFalse(isTypeAccepted(Object.class, AnyArrayType.class));
  }

  @Test
  public void testTrueArrayAcceptances() {
    assertTrue(isTypeAccepted(int[].class, OnlyIntArray.class));
    assertTrue(isTypeAccepted(String[].class, OnlyStringArray.class));
    assertTrue(isTypeAccepted(String[].class, CharSequence.class));
    assertTrue(isTypeAccepted(Boolean[].class, AnyArrayType.class));
    assertTrue(isTypeAccepted(Object[].class, Object.class));
  }

  @Test
  public void testFalseArrayAcceptances() {
    assertFalse(isTypeAccepted(Integer[].class, OnlyIntArray.class));
    assertFalse(isTypeAccepted(Object[].class, OnlyIntArray.class));
  }

}
