package tools.devnull.trugger;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OptionalTest {

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
  public void testMethodOrElse() {
    Runnable action = mock(Runnable.class);

    Optional.of(value).orElse(action);

    verify(action, never()).run();
  }

  @Test
  public void testMethodOrElseWithEmpty() {
    Runnable action = mock(Runnable.class);

    Optional.empty().orElse(action);

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

}
