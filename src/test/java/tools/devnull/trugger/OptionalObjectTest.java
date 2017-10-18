/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OptionalObjectTest {

  private Object value = new Object();

  @Test
  public void testValue() {
    assertSame(value, Optional.of(value).value());
    assertNull(Optional.empty().value());
    assertNull(Optional.empty(Object.class).value());
  }

  @Test
  public void testMatchingFilter() {
    Predicate predicate = mock(Predicate.class);
    when(predicate.test(value)).thenReturn(true);

    Optional optional = Optional.of(value).filter(predicate);

    verify(predicate).test(value);

    assertSame(value, optional.value());
  }

  @Test
  public void testMismatchingFilter() {
    Predicate predicate = mock(Predicate.class);
    when(predicate.test(value)).thenReturn(false);

    Optional optional = Optional.of(value).filter(predicate);

    verify(predicate).test(value);

    assertNull(optional.value());
  }

  @Test
  public void testMethodAnd() {
    Consumer consumer = mock(Consumer.class);

    Optional.of(value).and(consumer);

    verify(consumer).accept(value);
  }

  @Test
  public void testMethodAndWithEmpty() {
    Consumer consumer = mock(Consumer.class);

    Optional.empty().and(consumer);

    verify(consumer, never()).accept(any());
  }

  @Test
  public void testMethodMap() {
    Object newObject = new Object();
    Function function = mock(Function.class);
    when(function.apply(value)).thenReturn(newObject);

    Optional optional = Optional.of(value).map(function);

    verify(function).apply(value);
    assertSame(newObject, optional.value());
  }

  @Test
  public void testMethodMapWithEmpty() {
    Object newObject = new Object();
    Function function = mock(Function.class);
    when(function.apply(null)).thenReturn(newObject);

    Optional optional = Optional.empty().map(function);

    verify(function, never()).apply(any());
    assertNull(optional.value());
  }

  @Test
  public void testMethodOrElseDo() {
    Runnable action = mock(Runnable.class);

    Optional.of(value).orElseDo(action);

    verify(action, never()).run();
  }

  @Test
  public void testMethodOrElseWithEmpty() {
    Runnable action = mock(Runnable.class);

    Optional.empty().orElseDo(action);

    verify(action).run();
  }

  @Test
  public void testMethodOrElseReturn() {
    Object newObject = new Object();
    Supplier supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(newObject);

    Object object = Optional.of(this.value).orElseReturn(supplier);

    verify(supplier, never()).get();
    assertSame(value, object);
  }

  @Test
  public void testMethodContainsValue() {
    assertTrue(Optional.of(value).isPresent());
    assertFalse(Optional.empty().isPresent());
  }

  @Test
  public void testMethodOrElseReturnWithEmpty() {
    Object newObject = new Object();
    Supplier supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(newObject);

    Object object = Optional.empty().orElseReturn(supplier);

    verify(supplier).get();
    assertSame(newObject, object);
  }

  @Test
  public void testMethodOrElseThrow() {
    IllegalArgumentException exception = new IllegalArgumentException();
    Supplier supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(exception);

    Object object = Optional.of(this.value).orElseThrow(supplier);

    verify(supplier, never()).get();
    assertSame(value, object);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMethodOrElseThrowWithEmpty() {
    IllegalArgumentException exception = new IllegalArgumentException();
    Supplier supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(exception);

    Optional.empty().orElseThrow(supplier);
  }

  @Test
  public void testMethodThen() {
    Function function = mock(Function.class);
    when(function.apply(value)).thenReturn("OK");
    when(function.apply(null)).thenReturn("ERROR");

    assertEquals("OK", Optional.of(value).then(function));
    verify(function).apply(value);

    assertEquals("ERROR", Optional.empty().then(function));
    verify(function).apply(null);
  }

  @Test
  public void testMethodOrElse() {
    assertEquals(value, Optional.of(value).orElse(new Object()));
    assertEquals(value, Optional.empty().orElse(value));
  }

  @Test
  public void testWithSupplier() {
    Supplier supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(value);

    assertEquals(value, Optional.of(supplier).value());
    verify(supplier).get();
  }

}
